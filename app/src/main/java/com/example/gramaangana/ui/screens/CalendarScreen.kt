package com.example.gramaangana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.ui.components.SimpleTopBar
import com.example.gramaangana.ui.theme.*

enum class SlotStatus { FREE, PENDING, BOOKED }

@Composable
fun CalendarScreen(viewModel: AppViewModel, navController: NavController? = null) {
    var currentMonth by remember { mutableStateOf(5) }
    var currentYear by remember { mutableStateOf(2026) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }
    var showBookingDialog by remember { mutableStateOf(false) }
    var bookingSuccess by remember { mutableStateOf(false) }

    val slotStatusMap = viewModel.slotStatusMap
    val monthName = getMonthName(currentMonth)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        SimpleTopBar(
            viewModel = viewModel,
            onNotificationClick = { navController?.navigate("notifications") }
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Month navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Event Calendar",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    if (currentMonth == 1) { currentMonth = 12; currentYear-- }
                    else currentMonth--
                    selectedDay = null
                }) {
                    Icon(
                        Icons.Outlined.ChevronLeft, null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                ) {
                    Text(
                        "$monthName $currentYear",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = {
                    if (currentMonth == 12) { currentMonth = 1; currentYear++ }
                    else currentMonth++
                    selectedDay = null
                }) {
                    Icon(
                        Icons.Outlined.ChevronRight, null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Calendar Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "$monthName Schedule",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (selectedDay == null) {
                            Text(
                                "SELECT A DATE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    val dayHeaders = listOf("M", "T", "W", "T", "F", "S", "S")
                    Row(modifier = Modifier.fillMaxWidth()) {
                        dayHeaders.forEach { day ->
                            Text(
                                day,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    val firstDayOffset = getFirstDayOffset(currentMonth, currentYear)
                    val daysInMonth = getDaysInMonth(currentMonth, currentYear)
                    val rows = (firstDayOffset + daysInMonth + 6) / 7

                    for (row in 0 until rows) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (col in 0 until 7) {
                                val day = row * 7 + col - firstDayOffset + 1
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(2.dp)
                                        .aspectRatio(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (day in 1..daysInMonth) {
                                        val isSelected = selectedDay == day
                                        val status = slotStatusMap[day] ?: SlotStatus.FREE
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .then(
                                                    if (isSelected) Modifier.border(
                                                        1.5.dp, PrimaryIndigo,
                                                        RoundedCornerShape(8.dp)
                                                    ) else Modifier
                                                )
                                                .clickable {
                                                    selectedDay = day
                                                    bookingSuccess = false
                                                }
                                                .padding(4.dp)
                                        ) {
                                            Text(
                                                "$day",
                                                fontSize = 13.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold
                                                else FontWeight.Normal,
                                                color = if (isSelected) PrimaryIndigo
                                                else MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(Modifier.height(2.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        when (status) {
                                                            SlotStatus.FREE -> GreenFree
                                                            SlotStatus.PENDING -> AmberPending
                                                            SlotStatus.BOOKED -> RedBooked
                                                        }
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(0.4f)
                    )
                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LegendDot(GreenFree, "FREE")
                        Spacer(Modifier.width(16.dp))
                        LegendDot(AmberPending, "PENDING")
                        Spacer(Modifier.width(16.dp))
                        LegendDot(RedBooked, "BOOKED")
                    }
                }
            }

            // Day Details Card
            selectedDay?.let { day ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "DETAILS FOR",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    "${day}${getDaySuffix(day)} $monthName",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            val status = slotStatusMap[day] ?: SlotStatus.FREE
                            if (status != SlotStatus.BOOKED) {
                                Button(
                                    onClick = { showBookingDialog = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryIndigo
                                    )
                                ) {
                                    Text(
                                        "+ NEW REQUEST",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        if (bookingSuccess) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = GreenFree.copy(alpha = 0.15f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "✅ Booking request submitted! Awaiting approval.",
                                    modifier = Modifier.padding(16.dp),
                                    color = GreenFree,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                        }

                        val status = slotStatusMap[day] ?: SlotStatus.FREE
                        when (status) {
                            SlotStatus.FREE -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            Icons.Outlined.Info, null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                                .copy(alpha = 0.5f),
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            "No events planned for this day",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            SlotStatus.PENDING -> {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = AmberPending.copy(alpha = 0.15f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "⏳ A booking is pending approval for this day.",
                                        modifier = Modifier.padding(16.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            SlotStatus.BOOKED -> {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = RedBooked.copy(alpha = 0.15f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "🔴 This day is already booked. Please choose another date.",
                                        modifier = Modifier.padding(16.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    if (showBookingDialog) {
        selectedDay?.let { day ->
            BookingRequestDialog(
                day = day,
                monthName = monthName,
                onDismiss = { showBookingDialog = false },
                onSubmit = { eventName, timeSlot, contact ->
                    viewModel.addBooking(
                        day, currentMonth, currentYear,
                        eventName, timeSlot, contact
                    )
                    bookingSuccess = true
                    showBookingDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingRequestDialog(
    day: Int,
    monthName: String,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var eventName by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val timeSlots = listOf(
        "Morning (8:00 AM - 2:00 PM)",
        "Evening (2:00 PM - 8:00 PM)",
        "Full Day (8:00 AM - 8:00 PM)"
    )
    var selectedSlot by remember { mutableStateOf(timeSlots[0]) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "New Booking Request",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${day}${getDaySuffix(day)} $monthName",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "EVENT NAME",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = eventName,
                    onValueChange = { eventName = it },
                    placeholder = {
                        Text(
                            "Event Name",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = PrimaryIndigo,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    "TIME SLOT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedSlot,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = PrimaryIndigo,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        timeSlots.forEach { slot ->
                            DropdownMenuItem(
                                text = { Text(slot) },
                                onClick = { selectedSlot = slot; expanded = false }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "CONTACT DETAILS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    placeholder = {
                        Text(
                            "Mobile / Phone",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = PrimaryIndigo,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "CANCEL",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = {
                            if (eventName.isNotBlank() && contact.isNotBlank()) {
                                onSubmit(eventName, selectedSlot, contact)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryIndigo
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "SUBMIT",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

fun getMonthName(month: Int) = listOf(
    "", "January", "February", "March", "April", "May",
    "June", "July", "August", "September", "October", "November", "December"
)[month]

fun getDaysInMonth(month: Int, year: Int) = when (month) {
    2 -> if (year % 4 == 0) 29 else 28
    4, 6, 9, 11 -> 30
    else -> 31
}

fun getFirstDayOffset(month: Int, year: Int): Int {
    val cal = java.util.Calendar.getInstance()
    cal.set(year, month - 1, 1)
    return (cal.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7
}

fun getDaySuffix(day: Int) = when {
    day in 11..13 -> "th"
    day % 10 == 1 -> "st"
    day % 10 == 2 -> "nd"
    day % 10 == 3 -> "rd"
    else -> "th"
}