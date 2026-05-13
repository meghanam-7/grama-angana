package com.example.gramaangana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gramaangana.ui.components.AdminBottomNavBar
import com.example.gramaangana.ui.components.BottomNavBar
import com.example.gramaangana.ui.navigation.AppNavigation
import com.example.gramaangana.ui.theme.GramaAnganaTheme

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDark = viewModel.isDarkMode
            GramaAnganaTheme (darkTheme = isDark){

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val residentRoutes = listOf("home", "calendar", "maintenance", "sathi_ai")
                val adminRoutes = listOf(
                    "admin_home", "admin_bookings",
                    "admin_events", "admin_maintenance"
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        when {
                            currentRoute in residentRoutes ->
                                BottomNavBar(navController)
                            currentRoute in adminRoutes ->
                                AdminBottomNavBar(navController)
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}