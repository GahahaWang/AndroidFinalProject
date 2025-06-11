package com.example.sharedclassapp

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
import javax.security.auth.Subject

data class Friend(
    val id: Int,
    val name: String,
    val friendCode: String? = null,
    val subject: String? = null,
)

@Composable
fun ManageFriendListScreen(modifier: Modifier) {
    val viewModel: FriendViewModel = viewModel()
    val friendList = viewModel.friendList
    var showDialog by remember { mutableStateOf(false) }
    val sortedFriends = friendList.sortedWith(compareBy({ it.name }))

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    )
    {
        LazyColumn {
            items(sortedFriends) { friend ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
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

    if (showDialog) {
        AddFriendDialog(
            existingFriends = friendList,
            onAdd = {
                viewModel.addFriend(it)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = { showDialog = true },
            backgroundColor = Color(0xFF0D47A1),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Friend", tint = Color.White)
        }
    }
}

@Composable
fun AddFriendDialog(
    onAdd: (Friend) -> Unit,
    onDismiss: () -> Unit,
    existingFriends: List<Friend>
) {

}
