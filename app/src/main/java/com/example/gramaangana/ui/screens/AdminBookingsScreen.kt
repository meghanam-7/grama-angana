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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.BookingRequest
import com.example.gramaangana.ui.components.SimpleTopBar
import com.example.gramaangana.ui.theme.*

@Composable
fun AdminBookingsScreen(viewModel: AppViewModel) {
    val bookings = viewModel.bookingRequests
    var filterStatus by remember { mutableStateOf("ALL") }

    val filtered = when (filterStatus) {
        "PENDING" -> bookings.filter { it.status == SlotStatus.PENDING }
        "APPROVED" -> bookings.filter { it.status == SlotStatus.BOOKED }
        "REJECTED" -> bookings.filter { it.status == SlotStatus.FREE }
        else -> bookings.toList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        SimpleTopBar()
        HorizontalDivider(color = Color.LightGray.copy(0.4f))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Booking Requests",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )

            // Filter tabs
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("ALL", "PENDING", "APPROVED", "REJECTED").forEach { status ->
                    FilterChip(
                        selected = filterStatus == status,
                        onClick = { filterStatus = status },
                        label = { Text(status, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryIndigo,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No bookings found", color = TextSecondary)
                }
            } else {
                filtered.forEach { booking ->
                    AdminBookingCard(
                        booking = booking,
                        onApprove = { viewModel.approveBooking(booking.id) },
                        onReject = { viewModel.rejectBooking(booking.id) }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminBookingCard(
    booking: BookingRequest,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    val statusColor = when (booking.status) {
        SlotStatus.PENDING -> Color(0xFFF59E0B)
        SlotStatus.BOOKED -> Color(0xFF22C55E)
        SlotStatus.FREE -> Color(0xFFEF4444)
    }
    val statusText = when (booking.status) {
        SlotStatus.PENDING -> "PENDING"
        SlotStatus.BOOKED -> "APPROVED"
        SlotStatus.FREE -> "REJECTED"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        booking.eventName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "📅 ${booking.day}/${booking.month}/${booking.year}",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Text(
                        "⏰ ${booking.timeSlot}",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Text(
                        "👤 ${booking.bookedBy}",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Text(
                        "📞 ${booking.contact}",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = statusColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        statusText,
                        modifier = Modifier.padding(
                            horizontal = 10.dp, vertical = 4.dp
                        ),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            if (booking.status == SlotStatus.PENDING) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFEF4444)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Filled.Close, null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("REJECT", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onApprove,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF22C55E)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Filled.Check, null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("APPROVE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}