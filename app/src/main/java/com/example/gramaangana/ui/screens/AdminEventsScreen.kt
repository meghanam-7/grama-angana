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
import androidx.compose.ui.window.Dialog
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.CommunityEvent
import com.example.gramaangana.ui.components.SimpleTopBar
import com.example.gramaangana.ui.theme.*

@Composable
fun AdminEventsScreen(viewModel: AppViewModel) {
    val events = viewModel.communityEvents
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEvent by remember { mutableStateOf<CommunityEvent?>(null) }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Community Events",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                ) {
                    Icon(Icons.Filled.Add, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("ADD EVENT", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            if (events.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No events scheduled", color = TextSecondary)
                }
            } else {
                events.forEach { event ->
                    AdminEventCard(
                        event = event,
                        onEdit = { editingEvent = event },
                        onDelete = { viewModel.deleteEvent(event.id) }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }

    if (showAddDialog) {
        EventDialog(
            event = null,
            onDismiss = { showAddDialog = false },
            onSave = { event ->
                viewModel.addEvent(event)
                showAddDialog = false
            }
        )
    }

    editingEvent?.let { event ->
        EventDialog(
            event = event,
            onDismiss = { editingEvent = null },
            onSave = { updated ->
                viewModel.updateEvent(updated)
                editingEvent = null
            }
        )
    }
}

@Composable
fun AdminEventCard(
    event: CommunityEvent,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(event.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        if (event.isRecurring) {
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = PrimaryIndigo.copy(0.1f)
                            ) {
                                Text(
                                    "🔁 ${event.recurringType}",
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp, vertical = 2.dp
                                    ),
                                    fontSize = 10.sp,
                                    color = PrimaryIndigo,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(event.description, fontSize = 13.sp, color = TextSecondary)
                    Text(
                        "📅 ${event.day}/${event.month}/${event.year}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Text(
                        "⏰ ${event.timeSlot}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Filled.Edit, null,
                            tint = PrimaryIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Filled.Delete, null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDialog(
    event: CommunityEvent?,
    onDismiss: () -> Unit,
    onSave: (CommunityEvent) -> Unit
) {
    var title by remember { mutableStateOf(event?.title ?: "") }
    var description by remember { mutableStateOf(event?.description ?: "") }
    var day by remember { mutableStateOf(event?.day?.toString() ?: "") }
    var month by remember { mutableStateOf(event?.month?.toString() ?: "") }
    var year by remember { mutableStateOf(event?.year?.toString() ?: "2026") }
    var isRecurring by remember { mutableStateOf(event?.isRecurring ?: false) }
    var recurringType by remember { mutableStateOf(event?.recurringType ?: "weekly") }
    var expanded by remember { mutableStateOf(false) }
    val timeSlots = listOf(
        "Morning (8:00 AM - 2:00 PM)",
        "Evening (2:00 PM - 8:00 PM)",
        "Full Day (8:00 AM - 8:00 PM)"
    )
    var selectedSlot by remember { mutableStateOf(event?.timeSlot ?: timeSlots[0]) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    if (event == null) "Add Event" else "Edit Event",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Event Title") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = day,
                        onValueChange = { day = it },
                        label = { Text("Day") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = month,
                        onValueChange = { month = it },
                        label = { Text("Month") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it },
                        label = { Text("Year") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedSlot,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Time Slot") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
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

                // Recurring toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recurring Event", fontWeight = FontWeight.SemiBold)
                    Switch(
                        checked = isRecurring,
                        onCheckedChange = { isRecurring = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = PrimaryIndigo)
                    )
                }

                if (isRecurring) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("weekly", "monthly").forEach { type ->
                            FilterChip(
                                selected = recurringType == type,
                                onClick = { recurringType = type },
                                label = { Text(type.replaceFirstChar { it.uppercase() }) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrimaryIndigo,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CANCEL")
                    }
                    Button(
                        onClick = {
                            val d = day.toIntOrNull() ?: return@Button
                            val m = month.toIntOrNull() ?: return@Button
                            val y = year.toIntOrNull() ?: return@Button
                            onSave(
                                CommunityEvent(
                                    id = event?.id
                                        ?: System.currentTimeMillis().toString(),
                                    title = title,
                                    description = description,
                                    day = d, month = m, year = y,
                                    timeSlot = selectedSlot,
                                    isRecurring = isRecurring,
                                    recurringType = if (isRecurring) recurringType else ""
                                )
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("SAVE", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}