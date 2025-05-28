import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

//sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
//    object Home : Screen("home", "Home", Icons.Default.Home)
//    object Add : Screen("add", "Add", Icons.Default.Add)
//    object Friend : Screen("friend", "Friend", Icons.Default.Person)
//    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
//
//    companion object {
//        val all = listOf(Home, Add, Friend, Settings)
//    }
//}

data class ScreenItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

object Screen {
    val Add = ScreenItem("add", "Add", Icons.Filled.Add)
    val Home = ScreenItem("home", "Home", Icons.Filled.Home)
    val Settings = ScreenItem("settings", "Settings", Icons.Filled.Settings)
    val Friend = ScreenItem("friend", "Friends", Icons.Filled.Person)

    val all = listOf(Home, Add, Friend, Settings)
}
