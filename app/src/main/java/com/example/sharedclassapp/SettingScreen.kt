package com.example.sharedclassapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sharedclassapp.viewmodel.UserViewModel

@Composable
fun SettingScreen(modifier: Modifier = Modifier) {
    val userViewModel: UserViewModel = viewModel()
    val profile by userViewModel.profile.collectAsState()

    var name by remember { mutableStateOf(profile.name) }
    var school by remember { mutableStateOf(profile.school) }
    var subject by remember { mutableStateOf(profile.subject) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("使用者 UUID：${profile.uuid}", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("暱稱") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = school,
            onValueChange = { school = it },
            label = { Text("校名") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("科系") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { userViewModel.updateProfile(name, school, subject) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("儲存")
        }
    }
}