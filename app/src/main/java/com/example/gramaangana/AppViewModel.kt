package com.example.gramaangana

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.gramaangana.ui.screens.ChatMessage
import com.example.gramaangana.ui.screens.MaintenanceItem
import com.example.gramaangana.ui.screens.SlotStatus

// ── Auth models ──────────────────────────────────────────────
enum class UserRole { RESIDENT, ADMIN }
data class UserSession(val name: String, val role: UserRole)
data class RegisteredUser(
    val fullName: String,
    val email: String,
    val role: UserRole,
    val password: String
)
sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

// ── Booking model ────────────────────────────────────────────
data class BookingRequest(
    val id: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val eventName: String,
    val timeSlot: String,
    val contact: String,
    val bookedBy: String,
    var status: SlotStatus = SlotStatus.PENDING
)

// ── Event model ──────────────────────────────────────────────
data class CommunityEvent(
    val id: String,
    val title: String,
    val description: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val timeSlot: String,
    val isRecurring: Boolean = false,
    val recurringType: String = ""   // "weekly" or "monthly"
)

class AppViewModel : ViewModel() {

    // ── Auth ─────────────────────────────────────────────────
    private val registeredUsers = mutableListOf<RegisteredUser>()
    var currentUser by mutableStateOf<UserSession?>(null)
        private set

    var isDarkMode by mutableStateOf(false)

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }

    // ── Calendar ─────────────────────────────────────────────
    val slotStatusMap = mutableStateMapOf<Int, SlotStatus>().apply {
        put(3, SlotStatus.BOOKED)
        put(10, SlotStatus.PENDING)
    }

    // ── Bookings ─────────────────────────────────────────────
    val bookingRequests = mutableStateListOf(
        BookingRequest("1", 10, 5, 2026, "Yoga Class",
            "Morning (8:00 AM - 2:00 PM)", "9876543210", "Ravi Kumar"),
        BookingRequest("2", 15, 5, 2026, "Cultural Program",
            "Evening (2:00 PM - 8:00 PM)", "9123456789", "Priya Sharma"),
    )

    // ── Events ───────────────────────────────────────────────
    val communityEvents = mutableStateListOf(
        CommunityEvent("1", "Health Camp", "Free health checkup",
            20, 5, 2026, "Morning (8:00 AM - 2:00 PM)"),
        CommunityEvent("2", "Weekly Yoga", "Community yoga session",
            7, 5, 2026, "Morning (8:00 AM - 2:00 PM)",
            isRecurring = true, recurringType = "weekly"),
    )

    // ── Maintenance ──────────────────────────────────────────
    val maintenanceItems = mutableStateListOf(
        MaintenanceItem("1", "New Ceiling Fan",
            "Replace broken fan in main hall", 1500, 900),
        MaintenanceItem("2", "LED Bulbs (x10)",
            "Replace old tube lights", 500, 500),
    )

    // ── Chat ─────────────────────────────────────────────────
    val chatMessages = mutableStateListOf(
        ChatMessage(
            "Namaste! I am Grama Sathi, your community assistant. " +
                    "How can I help you today?",
            isUser = false
        )
    )

    // ── Notifications ────────────────────────────────────────
    val notifications = mutableStateListOf<String>()

    // Resident notifications (booking approvals/rejections)
    val residentNotifications = mutableStateListOf<String>()

    fun clearResidentNotification(index: Int) {
        if (index < residentNotifications.size) residentNotifications.removeAt(index)
    }

    // ── Auth functions ───────────────────────────────────────
    fun registerUser(
        fullName: String, email: String,
        role: UserRole, password: String
    ): AuthResult {
        if (registeredUsers.any { it.email.lowercase() == email.lowercase() })
            return AuthResult.Error("Email already registered. Please sign in.")
        registeredUsers.add(RegisteredUser(fullName, email, role, password))
        return AuthResult.Success
    }

    fun signIn(email: String, password: String): AuthResult {
        if (registeredUsers.isEmpty())
            return AuthResult.Error("No account found. Please sign up first.")
        val user = registeredUsers.find {
            it.email.lowercase() == email.lowercase()
        } ?: return AuthResult.Error("No account found. Please sign up first.")
        if (user.password != password)
            return AuthResult.Error("Incorrect password. Please try again.")
        currentUser = UserSession(user.fullName, user.role)
        return AuthResult.Success
    }

    fun signOut() { currentUser = null }

    // ── Resident functions ───────────────────────────────────
    fun addBooking(
        day: Int, month: Int, year: Int,
        eventName: String, timeSlot: String, contact: String
    ) {
        val existing = bookingRequests.any {
            it.day == day && it.month == month &&
                    it.year == year && it.timeSlot == timeSlot &&
                    it.status != SlotStatus.FREE
        }
        if (!existing) {
            bookingRequests.add(
                BookingRequest(
                    id = System.currentTimeMillis().toString(),
                    day = day, month = month, year = year,
                    eventName = eventName, timeSlot = timeSlot,
                    contact = contact,
                    bookedBy = currentUser?.name ?: "Unknown"
                )
            )
            slotStatusMap[day] = SlotStatus.PENDING
        }
    }

    fun pledge(itemId: String, amount: Int) {
        val index = maintenanceItems.indexOfFirst { it.id == itemId }
        if (index != -1) {
            val item = maintenanceItems[index]
            val newAmount = (item.collectedAmount + amount)
                .coerceAtMost(item.targetAmount)
            maintenanceItems[index] = item.copy(collectedAmount = newAmount)
            if (newAmount >= item.targetAmount) {
                notifications.add(
                    "🎉 Goal reached for '${item.title}'! Thank you for your support."
                )
            }
        }
    }

    fun addChatMessage(message: ChatMessage) { chatMessages.add(message) }

    // ── Admin functions ──────────────────────────────────────
    fun approveBooking(bookingId: String) {
        val index = bookingRequests.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            val booking = bookingRequests[index]
            bookingRequests[index] = booking.copy(status = SlotStatus.BOOKED)
            slotStatusMap[booking.day] = SlotStatus.BOOKED
            // Admin notification
            notifications.add(
                "✅ Booking approved: '${booking.eventName}' " +
                        "on ${booking.day}/${booking.month} for ${booking.bookedBy}"
            )
            // Resident notification
            residentNotifications.add(
                "✅ Your booking '${booking.eventName}' on " +
                        "${booking.day}/${booking.month} has been APPROVED!"
            )
        }
    }

    fun rejectBooking(bookingId: String) {
        val index = bookingRequests.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            val booking = bookingRequests[index]
            bookingRequests[index] = booking.copy(status = SlotStatus.FREE)
            slotStatusMap.remove(booking.day)
            // Admin notification
            notifications.add(
                "❌ Booking rejected: '${booking.eventName}' " +
                        "on ${booking.day}/${booking.month}"
            )
            // Resident notification
            residentNotifications.add(
                "❌ Your booking '${booking.eventName}' on " +
                        "${booking.day}/${booking.month} has been REJECTED."
            )
        }
    }

    fun addEvent(event: CommunityEvent) {
        communityEvents.add(event)
        slotStatusMap[event.day] = SlotStatus.BOOKED
    }

    fun updateEvent(updatedEvent: CommunityEvent) {
        val index = communityEvents.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) communityEvents[index] = updatedEvent
    }

    fun deleteEvent(eventId: String) {
        val event = communityEvents.find { it.id == eventId }
        event?.let {
            communityEvents.remove(it)
            slotStatusMap.remove(it.day)
        }
    }

    fun addMaintenanceItem(item: MaintenanceItem) {
        maintenanceItems.add(item)
    }

    fun removeMaintenanceItem(itemId: String) {
        maintenanceItems.removeAll { it.id == itemId }
    }

    fun clearNotification(index: Int) {
        if (index < notifications.size) notifications.removeAt(index)
    }
}