package com.example.gramaangana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.CommunityEvent
import com.example.gramaangana.ui.components.GramaTopBar
import com.example.gramaangana.ui.theme.AiBadgeBg
import com.example.gramaangana.ui.theme.AiBadgeText
import com.example.gramaangana.ui.theme.PrimaryIndigo
import com.example.gramaangana.ui.theme.PrimaryIndigoDark

@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    navController: NavController
) {
    val upcomingEvents = viewModel.communityEvents

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        GramaTopBar(
            viewModel = viewModel,
            onSignInClick = { navController.navigate("signin") },
            onNotificationClick = { navController.navigate("notifications") },
            onProfileClick = { navController.navigate("profile") }
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(listOf(PrimaryIndigoDark, PrimaryIndigo))
                    )
                    .padding(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.CenterEnd)
                        .offset(x = 30.dp)
                        .background(
                            Color.White.copy(alpha = 0.07f),
                            RoundedCornerShape(40.dp)
                        )
                )
                Column {
                    Text(
                        "GRAMA ANGANA",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Community Space",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.LocationOn, null,
                                tint = Color.White.copy(0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "Main Road, Grama",
                                color = Color.White.copy(0.9f),
                                fontSize = 13.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Schedule, null,
                                tint = Color.White.copy(0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "8 AM – 10 PM",
                                color = Color.White.copy(0.9f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Upcoming Events Board
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Upcoming Events Board",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = AiBadgeBg,
                            border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                        ) {
                            Text(
                                "AI SUMMARIZED",
                                modifier = Modifier.padding(
                                    horizontal = 10.dp, vertical = 4.dp
                                ),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AiBadgeText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (upcomingEvents.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Filled.CalendarMonth,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        .copy(alpha = 0.4f),
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "No upcoming events listed",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        upcomingEvents.forEach { event ->
                            EventListItem(event)
                            if (event != upcomingEvents.last()) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outline.copy(0.3f),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun EventListItem(event: CommunityEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = PrimaryIndigo.copy(alpha = 0.15f),
            modifier = Modifier.size(48.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "${event.day}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = PrimaryIndigo
                )
                Text(
                    getMonthName(event.month).take(3).uppercase(),
                    fontSize = 9.sp,
                    color = PrimaryIndigo,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (event.isRecurring) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PrimaryIndigo.copy(0.1f)
                    ) {
                        Text(
                            "🔁 ${event.recurringType}",
                            modifier = Modifier.padding(
                                horizontal = 6.dp, vertical = 2.dp
                            ),
                            fontSize = 9.sp,
                            color = PrimaryIndigo,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Text(
                event.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "⏰ ${event.timeSlot}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}