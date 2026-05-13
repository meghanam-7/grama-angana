package com.example.gramaangana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.ui.theme.PrimaryIndigo
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val isUser: Boolean)

@Composable
fun SathiAiScreen(viewModel: AppViewModel, navController: NavController? = null) {
    val messages = viewModel.chatMessages
    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryIndigo)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            Icons.Filled.SmartToy, null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Grama Sathi",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    if (isTyping) {
                        Text(
                            "typing...",
                            color = Color.White.copy(0.7f),
                            fontSize = 11.sp
                        )
                    } else {
                        Text(
                            "AI SUPPORT",
                            color = Color.White.copy(0.7f),
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
                // Notification bell
                BadgedBox(
                    badge = {
                        if (viewModel.residentNotifications.isNotEmpty()) {
                            Badge {
                                Text("${viewModel.residentNotifications.size}")
                            }
                        }
                    }
                ) {
                    IconButton(onClick = { navController?.navigate("notifications") }) {
                        Icon(
                            Icons.Filled.Notifications, null,
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Chat messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(messages) { _, msg ->
                ChatBubble(msg)
            }
        }

        // Input area
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text(
                        "Ask about bookings or repairs...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = PrimaryIndigo,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 2
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (inputText.isNotBlank() && !isTyping) {
                        val userMsg = inputText.trim()
                        viewModel.addChatMessage(ChatMessage(userMsg, isUser = true))
                        inputText = ""
                        isTyping = true
                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size - 1)
                            kotlinx.coroutines.delay(800)
                            val reply = getSathiReply(userMsg)
                            viewModel.addChatMessage(ChatMessage(reply, isUser = false))
                            isTyping = false
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryIndigo.copy(alpha = 0.15f))
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send, null,
                    tint = PrimaryIndigo
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isUser) {
            Icon(
                Icons.Filled.SmartToy, null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(28.dp)
                    .padding(end = 4.dp)
            )
        }
        Surface(
            shape = RoundedCornerShape(
                topStart = if (message.isUser) 16.dp else 4.dp,
                topEnd = if (message.isUser) 4.dp else 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            color = if (message.isUser)
                PrimaryIndigo
            else
                MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                message.text,
                modifier = Modifier.padding(12.dp),
                fontSize = 14.sp,
                color = if (message.isUser)
                    Color.White
                else
                    MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )
        }
    }
}

fun getSathiReply(input: String): String {
    val lower = input.lowercase()
    return when {
        "book" in lower || "booking" in lower ->
            "You can book the hall from the Calendar tab! Select a free date (green dot) and tap '+ New Request'."
        "repair" in lower || "maintenance" in lower ->
            "Check the Maintenance tab to see ongoing repair needs and pledge your support."
        "free" in lower || "available" in lower ->
            "Green dots mean free, yellow means pending, red means booked."
        "time" in lower || "hours" in lower ->
            "Grama Angana is open 8 AM to 10 PM. Morning: 8AM-2PM, Evening: 2PM-8PM."
        "hello" in lower || "hi" in lower || "namaste" in lower ->
            "Namaste! 🙏 How can I help you today?"
        "cancel" in lower ->
            "To cancel a booking, please contact the Panchayat office directly."
        "cost" in lower || "fee" in lower ->
            "The hall is free for community events. Contact Panchayat for private event fees."
        "capacity" in lower || "how many" in lower ->
            "The hall can accommodate up to 200 people."
        "thank" in lower ->
            "You're welcome! 🙏 Feel free to ask anything else."
        else ->
            "I can help with bookings, maintenance, and general queries. Could you rephrase?"
    }
}