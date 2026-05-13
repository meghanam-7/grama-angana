package com.example.gramaangana.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.UserRole
import com.example.gramaangana.ui.screens.*
import com.example.gramaangana.ui.theme.PrimaryIndigo
import com.example.gramaangana.ui.theme.TextSecondary

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    var showLoginAlert by remember { mutableStateOf(false) }

    if (showLoginAlert) {
        AlertDialog(
            onDismissRequest = { showLoginAlert = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            icon = {
                Icon(
                    Icons.Filled.Lock, null,
                    tint = PrimaryIndigo,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    "Sign In Required",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PrimaryIndigo,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    "You need to sign in first to access this feature.",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLoginAlert = false
                        navController.navigate("signin")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("SIGN IN NOW", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLoginAlert = false },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CANCEL", fontWeight = FontWeight.Bold, color = TextSecondary)
                }
            }
        )
    }

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        // ── Public screens ──────────────────────────────────────
        composable("home") {
            HomeScreen(viewModel = viewModel, navController = navController)
        }
        composable("notifications") {
            NotificationsScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("signin") {
            SignInScreen(
                viewModel = viewModel,
                onSignInSuccess = {
                    if (viewModel.currentUser?.role == UserRole.ADMIN) {
                        navController.navigate("admin_home") {
                            popUpTo("signin") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("signin") { inclusive = true }
                        }
                    }
                },
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignUpScreen(
                viewModel = viewModel,
                onSignUpSuccess = {
                    navController.navigate("signin") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onNavigateToSignIn = { navController.popBackStack() }
            )
        }

        // ── Resident screens ────────────────────────────────────
        composable("calendar") {
            if (viewModel.currentUser != null) {
                CalendarScreen(viewModel, navController)
            } else {
                HomeScreen(viewModel = viewModel, navController = navController)
                LaunchedEffect(Unit) { showLoginAlert = true }
            }
        }
        composable("maintenance") {
            if (viewModel.currentUser != null) {
                MaintenanceScreen(viewModel, navController)
            } else {
                HomeScreen(viewModel = viewModel, navController = navController)
                LaunchedEffect(Unit) { showLoginAlert = true }
            }
        }
        composable("sathi_ai") {
            if (viewModel.currentUser != null) {
                SathiAiScreen(viewModel, navController)
            } else {
                HomeScreen(viewModel = viewModel, navController = navController)
                LaunchedEffect(Unit) { showLoginAlert = true }
            }
        }

        // ── Admin screens ───────────────────────────────────────
        composable("admin_home") {
            if (viewModel.currentUser?.role == UserRole.ADMIN) {
                AdminHomeScreen(viewModel = viewModel, navController = navController)
            } else {
                navController.navigate("home")
            }
        }

        composable("profile") {
            if (viewModel.currentUser != null) {
                ProfileScreen(viewModel = viewModel, navController = navController)
            } else {
                navController.navigate("home")
            }
        }

        composable("admin_bookings") {
            if (viewModel.currentUser?.role == UserRole.ADMIN) {
                AdminBookingsScreen(viewModel = viewModel)
            }
        }
        composable("admin_events") {
            if (viewModel.currentUser?.role == UserRole.ADMIN) {
                AdminEventsScreen(viewModel = viewModel)
            }
        }
        composable("admin_maintenance") {
            if (viewModel.currentUser?.role == UserRole.ADMIN) {
                AdminMaintenanceScreen(viewModel = viewModel)
            }
        }
    }
}