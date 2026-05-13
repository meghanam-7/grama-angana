package com.example.gramaangana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.ui.theme.*

@Composable
fun AdminHomeScreen(
    viewModel: AppViewModel,
    navController: NavController
) {
    val pendingCount = viewModel.bookingRequests.count {
        it.status == com.example.gramaangana.ui.screens.SlotStatus.PENDING
    }
    val eventsCount = viewModel.communityEvents.size
    val maintenanceCount = viewModel.maintenanceItems.size
    val notifications = viewModel.notifications

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
    ) {
        // Admin Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(PrimaryIndigoDark, PrimaryIndigo))
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Admin Dashboard",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            "👋 Welcome, ${viewModel.currentUser?.name}",
                            fontSize = 13.sp,
                            color = Color.White.copy(0.8f)
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            viewModel.signOut()
                            navController.navigate("home") {
                                popUpTo("admin_home") { inclusive = true }
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                    ) {
                        Text(
                            "SIGN OUT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats cards
            Text("Overview", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminStatCard(
                    icon = Icons.Filled.Pending,
                    title = "Pending",
                    value = "$pendingCount",
                    subtitle = "Bookings",
                    color = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    icon = Icons.Filled.CalendarMonth,
                    title = "Events",
                    value = "$eventsCount",
                    subtitle = "Scheduled",
                    color = PrimaryIndigo,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    icon = Icons.Filled.Build,
                    title = "Tasks",
                    value = "$maintenanceCount",
                    subtitle = "Maintenance",
                    color = Color(0xFF22C55E),
                    modifier = Modifier.weight(1f)
                )
            }

            // Quick Actions
            Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            AdminActionCard(
                icon = Icons.Filled.Task,
                title = "Manage Bookings",
                subtitle = "$pendingCount pending approval",
                color = Color(0xFFF59E0B),
                onClick = { navController.navigate("admin_bookings") }
            )
            AdminActionCard(
                icon = Icons.Filled.CalendarMonth,
                title = "Manage Events",
                subtitle = "Add, edit or remove events",
                color = PrimaryIndigo,
                onClick = { navController.navigate("admin_events") }
            )
            AdminActionCard(
                icon = Icons.Filled.Build,
                title = "Manage Maintenance",
                subtitle = "Add or remove maintenance tasks",
                color = Color(0xFF22C55E),
                onClick = { navController.navigate("admin_maintenance") }
            )

            // Notifications
            if (notifications.isNotEmpty()) {
                Text("Notifications", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                notifications.forEachIndexed { index, msg ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                msg,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.clearNotification(index) }
                            ) {
                                Icon(
                                    Icons.Filled.Close, null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminStatCard(
    icon: ImageVector,
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = color)
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(subtitle, fontSize = 10.sp, color = TextSecondary)
        }
    }
}

@Composable
fun AdminActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, fontSize = 12.sp, color = TextSecondary)
            }
            Icon(
                Icons.Filled.ChevronRight, null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}