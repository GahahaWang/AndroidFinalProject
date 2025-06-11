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
    var scannedCourses by remember { mutableStateOf<List<Course>?>(null) }
    var newFriendName by remember { mutableStateOf("") }

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
                    val courseList = Gson().fromJson(decoded, Array<Course>::class.java).toList()
                    scannedCourses = courseList
                    showAddDialog = true
                } catch (e: Exception) {
                    Toast.makeText(context, "解析課表失敗", Toast.LENGTH_SHORT).show()
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
    if (showAddDialog && scannedCourses != null) {
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
                            subject = Gson().toJson(scannedCourses)
                        )
                    )
                    showAddDialog = false
                    newFriendName = ""
                    scannedCourses = null
                }) { Text("新增") }
            },
            dismissButton = {
                Button(onClick = {
                    showAddDialog = false
                    newFriendName = ""
                    scannedCourses = null
                }) { Text("取消") }
            }
        )
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
                            navController.navigate("friendCourse/${friend.id}")
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
                            Text(friend.name)
                            Text(friend.subject ?: "無科目")
                            Text(friend.friendCode ?: "無好友碼")
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
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        val intent = Intent(context, QrScanActivity::class.java)
                        scanLauncher.launch(intent)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                backgroundColor = Color(0xFF388E3C)
            ) {
                Icon(Icons.Default.QrCode, contentDescription = "Scan QR", tint = Color.White)
            }
        }
    }
}