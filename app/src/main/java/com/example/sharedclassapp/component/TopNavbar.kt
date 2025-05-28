import androidx.compose.material.TopAppBar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TopBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when (currentRoute) {
        "home" -> "Welcome, Guest"
        "add" -> "My Courses"
        "friend" -> "Friend List"
        "settings" -> "Settings"
        else -> ""
    }

    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = Color(0xFF2196F3),
        contentColor = Color.White
    )
}
