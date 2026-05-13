package com.example.gramaangana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.ui.theme.*
import androidx.compose.material3.MaterialTheme

@Composable
fun ProfileScreen(
    viewModel: AppViewModel,
    navController: NavController
) {
    val user = viewModel.currentUser
    val myBookings = viewModel.bookingRequests.filter {
        it.bookedBy == user?.name
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(PrimaryIndigoDark, PrimaryIndigo))
                )
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, null, tint = Color.White)
                    }
                    Text(
                        "My Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Avatar circle
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(0.2f),
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.Person, null,
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    user?.name ?: "Guest",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(0.2f)
                ) {
                    Text(
                        user?.role?.name ?: "",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val total = myBookings.size
                val approved = myBookings.count { it.status == SlotStatus.BOOKED }
                val pending = myBookings.count { it.status == SlotStatus.PENDING }
                val rejected = myBookings.count { it.status == SlotStatus.FREE }

                ProfileStatCard("Total", "$total", PrimaryIndigo, Modifier.weight(1f))
                ProfileStatCard("Approved", "$approved", GreenFree, Modifier.weight(1f))
                ProfileStatCard("Pending", "$pending", AmberPending, Modifier.weight(1f))
                ProfileStatCard("Rejected", "$rejected", RedBooked, Modifier.weight(1f))
            }

            // Booking history
            Text(
                "My Booking History",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            if (myBookings.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "You haven't made any bookings yet.",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                myBookings.sortedByDescending { it.id }.forEach { booking ->
                    val statusColor = when (booking.status) {
                        SlotStatus.BOOKED -> GreenFree
                        SlotStatus.PENDING -> AmberPending
                        SlotStatus.FREE -> RedBooked
                    }
                    val statusText = when (booking.status) {
                        SlotStatus.BOOKED -> "APPROVED"
                        SlotStatus.PENDING -> "PENDING"
                        SlotStatus.FREE -> "REJECTED"
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    booking.eventName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "📅 ${booking.day}/${booking.month}/${booking.year}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    "⏰ ${booking.timeSlot}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = statusColor.copy(0.1f)
                            ) {
                                Text(
                                    statusText,
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp, vertical = 5.dp
                                    ),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor
                                )
                            }
                        }
                    }
                }
            }

            // Dark Mode toggle card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🌙", fontSize = 24.sp)
                        Column {
                            Text("Dark Mode", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Switch app appearance", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                    Switch(
                        checked = viewModel.isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PrimaryIndigo,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }
            }

            // Sign Out button
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.signOut()
                    navController.navigate("home") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444)
                ),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(
                    "SIGN OUT",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileStatCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = color)
            Text(label, fontSize = 10.sp, color = TextSecondary)
        }
    }
}