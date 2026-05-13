package com.example.gramaangana.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.ui.theme.PrimaryIndigo
import com.example.gramaangana.ui.theme.TextOnPrimary
import com.example.gramaangana.ui.theme.TextSecondary
import androidx.compose.material.icons.filled.AccountCircle

@Composable
fun GramaTopBar(
    viewModel: AppViewModel,
    onSignInClick: () -> Unit,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val notificationCount = viewModel.residentNotifications.size

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            androidx.compose.material3.Text(
                text = "Grama Angana",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryIndigo
            )
            if (viewModel.currentUser != null) {
                androidx.compose.material3.Text(
                    text = "👋 ${viewModel.currentUser!!.name} · ${viewModel.currentUser!!.role.name}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onProfileClick) {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    tint = PrimaryIndigo,
                    modifier = Modifier.size(28.dp)
                )
            }
            // Notification bell — only show when signed in
            if (viewModel.currentUser != null) {
                BadgedBox(
                    badge = {
                        if (notificationCount > 0) {
                            Badge { Text("$notificationCount") }
                        }
                    }
                ) {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = PrimaryIndigo
                        )
                    }
                }

                // Sign Out button
                OutlinedButton(
                    onClick = { viewModel.signOut() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PrimaryIndigo
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Filled.Logout,
                        null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    androidx.compose.material3.Text(
                        "OUT",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            } else {
                Button(
                    onClick = onSignInClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    androidx.compose.material3.Text(
                        "SIGN IN",
                        color = TextOnPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// Used on Calendar, Maintenance, Sathi AI screens
@Composable
fun SimpleTopBar(
    viewModel: AppViewModel? = null,
    onNotificationClick: () -> Unit = {}
) {
    val notificationCount = viewModel?.residentNotifications?.size ?: 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Text(
            text = "Grama Angana",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryIndigo
        )

        if (viewModel?.currentUser != null) {
            BadgedBox(
                badge = {
                    if (notificationCount > 0) {
                        Badge { Text("$notificationCount") }
                    }
                }
            ) {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = PrimaryIndigo
                    )
                }
            }
        }
    }
}