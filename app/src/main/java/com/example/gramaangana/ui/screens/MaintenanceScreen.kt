package com.example.gramaangana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.ui.components.SimpleTopBar
import com.example.gramaangana.ui.theme.AmberPending
import com.example.gramaangana.ui.theme.GreenFree
import com.example.gramaangana.ui.theme.PrimaryIndigo

data class MaintenanceItem(
    val id: String,
    val title: String,
    val description: String,
    val targetAmount: Int,
    val collectedAmount: Int
)

@Composable
fun MaintenanceScreen(viewModel: AppViewModel, navController: NavController? = null) {
    val items = viewModel.maintenanceItems
    var pledgeItem by remember { mutableStateOf<MaintenanceItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        SimpleTopBar(
            viewModel = viewModel,
            onNotificationClick = { navController?.navigate("notifications") }
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Maintenance Jar",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Support village infrastructure maintenance.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )

            if (items.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Outlined.Favorite, null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    .copy(alpha = 0.4f),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "No active maintenance tasks",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items.forEach { item ->
                    MaintenanceItemCard(
                        item = item,
                        onPledge = { pledgeItem = item }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }

    pledgeItem?.let { item ->
        PledgeDialog(
            item = item,
            onDismiss = { pledgeItem = null },
            onConfirm = { amount ->
                viewModel.pledge(item.id, amount)
                pledgeItem = null
            }
        )
    }
}

@Composable
fun PledgeDialog(
    item: MaintenanceItem,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var selectedAmount by remember { mutableStateOf(100) }
    val amounts = listOf(50, 100, 200, 500)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Pledge Support",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "For: ${item.title}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "SELECT AMOUNT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    amounts.forEach { amount ->
                        val isSelected = selectedAmount == amount
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected)
                                PrimaryIndigo
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedAmount = amount }
                        ) {
                            Text(
                                "₹$amount",
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isSelected)
                                    Color.White
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

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
                        onClick = { onConfirm(selectedAmount) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CONFIRM", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun MaintenanceItemCard(
    item: MaintenanceItem,
    onPledge: () -> Unit
) {
    val progress = item.collectedAmount.toFloat() / item.targetAmount.toFloat()
    val isComplete = item.collectedAmount >= item.targetAmount

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
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        item.description,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
                if (isComplete) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = GreenFree.copy(alpha = 0.15f)
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
                }
            }

            Spacer(Modifier.height(12.dp))

            // Progress bar track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!isComplete) {
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onPledge,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "PLEDGE SUPPORT",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}