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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.ui.components.SimpleTopBar
import com.example.gramaangana.ui.theme.*

@Composable
fun AdminMaintenanceScreen(viewModel: AppViewModel) {
    val items = viewModel.maintenanceItems
    var showAddDialog by remember { mutableStateOf(false) }

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
                    "Maintenance Tasks",
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
                    Text("ADD TASK", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No maintenance tasks", color = TextSecondary)
                }
            } else {
                items.forEach { item ->
                    AdminMaintenanceCard(
                        item = item,
                        onDelete = { viewModel.removeMaintenanceItem(item.id) }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }

    if (showAddDialog) {
        AddMaintenanceDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { item ->
                viewModel.addMaintenanceItem(item)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AdminMaintenanceCard(
    item: MaintenanceItem,
    onDelete: () -> Unit
) {
    val progress = item.collectedAmount.toFloat() / item.targetAmount.toFloat()
    val isComplete = item.collectedAmount >= item.targetAmount

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
                    Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(item.description, fontSize = 13.sp, color = TextSecondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isComplete) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = GreenFree.copy(0.1f)
                        ) {
                            Text(
                                "COMPLETE",
                                modifier = Modifier.padding(
                                    horizontal = 8.dp, vertical = 4.dp
                                ),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenFree
                            )
                        }
                        Spacer(Modifier.width(4.dp))
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

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BackgroundGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isComplete) GreenFree else PrimaryIndigo)
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "₹${item.collectedAmount} raised",
                    fontSize = 13.sp,
                    color = PrimaryIndigo,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Goal: ₹${item.targetAmount}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun AddMaintenanceDialog(
    onDismiss: () -> Unit,
    onAdd: (MaintenanceItem) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Add Maintenance Task",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorMsg = "" },
                    label = { Text("Task Title") },
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

                OutlinedTextField(
                    value = targetAmount,
                    onValueChange = { targetAmount = it; errorMsg = "" },
                    label = { Text("Target Amount (₹)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red, fontSize = 12.sp)
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
                            when {
                                title.isBlank() ->
                                    errorMsg = "Please enter a title"
                                targetAmount.toIntOrNull() == null ->
                                    errorMsg = "Please enter a valid amount"
                                else -> {
                                    onAdd(
                                        MaintenanceItem(
                                            id = System.currentTimeMillis().toString(),
                                            title = title,
                                            description = description,
                                            targetAmount = targetAmount.toInt(),
                                            collectedAmount = 0
                                        )
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryIndigo
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("ADD", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}