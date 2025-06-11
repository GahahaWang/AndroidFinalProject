package com.example.sharedclassapp

import BottomNavBar
import FriendCourseScreen
import TopBar
import Screen
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sharedclassapp.viewmodel.FriendViewModel
import com.google.gson.Gson

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(modifier = Modifier) }
            composable(Screen.Add.route) { ManageCourseListScreen(modifier = Modifier) }
            composable(Screen.Friend.route) { 
                ManageFriendListScreen(modifier = Modifier, navController = navController) 
            }
            composable(Screen.Settings.route) { Text("Settings Page") }
            composable("friendCourse/{friendId}") { backStackEntry ->
                val friendId = backStackEntry.arguments?.getString("friendId")?.toIntOrNull()
                val viewModel: FriendViewModel = viewModel()
                val friendList = viewModel.friendList
                val friend = friendList.find { it.id == friendId }
                if (friend != null && friend.subject != null) {
                    val courseList = Gson().fromJson(friend.subject, Array<Course>::class.java).toList()
                    FriendCourseScreen(courseList, Modifier)
                }
            }
        }
    }
}