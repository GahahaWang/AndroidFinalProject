package com.example.sharedclassapp

import BottomNavBar
import FriendCourseScreen
import TopBar
import Screen
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sharedclassapp.viewmodel.FriendViewModel

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
            composable(Screen.Settings.route) { SettingScreen(modifier = Modifier) }
            composable("friendCourse/{friendCode}") { backStackEntry ->
                val friendCode = backStackEntry.arguments?.getString("friendCode")
                val viewModel: FriendViewModel = viewModel()
                val friend = viewModel.friendList.find { it.friendCode == friendCode }
                if (friend!= null) {
                    FriendCourseScreen(friend = friend, modifier = Modifier)
                }
            }
        }
    }
}