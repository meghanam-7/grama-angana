package com.example.gramaangana.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gramaangana.ui.theme.PrimaryIndigo
import com.example.gramaangana.ui.theme.TextSecondary

data class AdminNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun AdminBottomNavBar(navController: NavController) {
    val items = listOf(
        AdminNavItem("DASHBOARD", Icons.Filled.Dashboard, "admin_home"),
        AdminNavItem("BOOKINGS", Icons.Filled.Task, "admin_bookings"),
        AdminNavItem("EVENTS", Icons.Filled.CalendarMonth, "admin_events"),
        AdminNavItem("MAINTENANCE", Icons.Filled.Settings, "admin_maintenance"),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(90.dp)
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo("admin_home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(21.dp)
                    )
                },
                label = { Text(item.label, fontSize = 8.sp) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryIndigo,
                    selectedTextColor = PrimaryIndigo,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}