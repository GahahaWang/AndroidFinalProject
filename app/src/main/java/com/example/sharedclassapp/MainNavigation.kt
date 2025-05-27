package com.example.sharedclassapp

import BottomNavBar
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            if (navController.currentDestination?.route == Screen.Add.route) {
                FloatingActionButton(onClick = { /* handle dialog or add logic */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Add.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { Text("Home Page") }
            composable(Screen.Add.route) { ManageCourseListScreen(modifier=Modifier) }
            composable(Screen.Friend.route) { Text("Friends Page") }
            composable(Screen.Settings.route) { Text("Settings Page") }
        }
    }
}
