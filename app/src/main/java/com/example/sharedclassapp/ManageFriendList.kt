package com.example.sharedclassapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import com.example.sharedclassapp.Course
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sharedclassapp.viewmodel.FriendViewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import javax.security.auth.Subject
import android.util.Base64
import androidx.navigation.NavController

data class Friend(
    val id: Int,
    val name: String,
    val friendCode: String? = null,
    val subject: String? = null,
    val school: String? = null,
    val courseListJson: String? = null // 新增課表欄位
)

@Composable
fun ManageFriendListScreen(
    modifier: Modifier,
    navController: NavController // 新增這個參數
) {
    val viewModel: FriendViewModel = viewModel()
    val friendList = viewModel.friendList
    val context = LocalContext.current

    var showAddDialog by remember { mutableStateOf(false) }
    var newFriendName by remember { mutableStateOf("") }
    var newFriendCode by remember { mutableStateOf("") }
    var newFriendSubject by remember { mutableStateOf("") }
    var newFriendSchool by remember { mutableStateOf("") }
    var newFriendCourseListJson by remember { mutableStateOf("") }

    // 掃描 QRCode
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val contents = result.data?.getStringExtra("SCAN_RESULT")
            if (contents != null) {
                // Base64 解碼
                try {
                    val decoded = String(Base64.decode(contents, Base64.DEFAULT), Charsets.UTF_8)
                    val jsonObj = Gson().fromJson(decoded, Map::class.java)
                    val name = jsonObj["name"] as? String ?: "無名氏"
                    val friendCode = jsonObj["friendCode"] as? String ?: ""
                    val subject = jsonObj["subject"] as? String ?: "未知科系"
                    val school = jsonObj["school"] as? String ?: "未知學校"
                    // 這裡修正 ↓↓↓
                    val coursesListJson = when (val c = jsonObj["courses"]) {
                        is String -> c
                        else -> Gson().toJson(c) // 如果不是字串就轉成字串
                    }
                    newFriendName = name
                    newFriendCode = friendCode
                    newFriendSubject = subject
                    newFriendSchool = school
                    newFriendCourseListJson = coursesListJson
                    showAddDialog = true
                } catch (e: Exception) {
                    Toast.makeText(context, "解析課表失敗", Toast.LENGTH_SHORT).show()
                    print(e)
                }
            }
        }
    }

    // 權限請求
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(context, QrScanActivity::class.java)
            scanLauncher.launch(intent)
        } else {
            Toast.makeText(context, "請允許相機權限以掃描 QRCode", Toast.LENGTH_SHORT).show()
        }
    }

    // 新增朋友 Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("新增朋友") },
            text = {
                Column {
                    Text("請輸入朋友名稱：")
                    OutlinedTextField(
                        value = newFriendName,
                        onValueChange = { newFriendName = it },
                        label = { Text("朋友名稱") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addFriend(
                        Friend(
                            id = 0,
                            name = newFriendName,
                            subject = newFriendSubject,
                            friendCode = newFriendCode,
                            school = newFriendSchool,
                            courseListJson = newFriendCourseListJson // 新增這行
                        )
                    )
                    showAddDialog = false
                    newFriendName = ""
                    newFriendCode = ""
                    newFriendSubject = ""
                    newFriendSchool = ""
                    newFriendCourseListJson = ""
                }) { Text("新增") }
            },
            dismissButton = {
                Button(onClick = {
                    showAddDialog = false
                    newFriendName = ""
                    newFriendCode = ""
                    newFriendSubject = ""
                    newFriendSchool = ""
                    newFriendCourseListJson = ""
                }) { Text("取消") }
            }
        )
    }

    var showScanChoiceDialog by remember { mutableStateOf(false) }

    // 新增相簿選擇的 launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            // 這裡你可以將 uri 傳給你的 QrScanActivity 或直接處理圖片解碼
            val intent = Intent(context, QrScanActivity::class.java).apply {
                putExtra("IMAGE_URI", uri.toString())
            }
            scanLauncher.launch(intent)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        LazyColumn {
            items(friendList) { friend ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            // 導航到 FriendCourseScreen
                            navController.navigate("friendCourse/${friend.friendCode}")
                        },
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color(0xFFE0E0E0)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (friend.friendCode.isNullOrBlank()) friend.name
                                else "${friend.name} (${friend.friendCode})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(friend.school ?: "未知學校")
                            Text(friend.subject ?: "無科目")
                        }
                        IconButton(
                            onClick = { viewModel.deleteFriend(friend) },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.Red, shape = CircleShape)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            FloatingActionButton(
                onClick = {
                    showScanChoiceDialog = true
                },
                backgroundColor = Color(0xFF388E3C)
            ) {
                Icon(Icons.Default.QrCode, contentDescription = "Scan QR", tint = Color.White)
            }
        }
    }

    // 彈出選擇來源的 Dialog
    if (showScanChoiceDialog) {
        AlertDialog(
            onDismissRequest = { showScanChoiceDialog = false },
            title = { Text("選擇掃描方式") },
            text = {
                Column {
                    Button(
                        onClick = {
                            showScanChoiceDialog = false
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_GRANTED
                            ) {
                                val intent = Intent(context, QrScanActivity::class.java)
                                scanLauncher.launch(intent)
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("相機掃描")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showScanChoiceDialog = false
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("從相簿選擇")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                Button(onClick = { showScanChoiceDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}