package com.example.gramaangana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.AuthResult
import com.example.gramaangana.UserRole
import com.example.gramaangana.ui.theme.*

@Composable
fun SignUpScreen(
    viewModel: AppViewModel,
    onSignUpSuccess: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.RESIDENT) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var successMsg by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(listOf(PrimaryIndigoDark, PrimaryIndigo))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(40.dp))

            Text(
                "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                "Join the Grama Angana community",
                fontSize = 14.sp,
                color = Color.White.copy(0.8f)
            )

            Spacer(Modifier.height(8.dp))

            // Full Name
            SignUpField(
                label = "FULL NAME",
                value = fullName,
                placeholder = "Enter your full name",
                onValueChange = { fullName = it; errorMsg = "" }
            )

            // Email
            SignUpField(
                label = "EMAIL",
                value = email,
                placeholder = "your@email.com",
                onValueChange = { email = it; errorMsg = "" }
            )

            // Role selector
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "ROLE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.8f),
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SignUpRoleCard(
                        icon = Icons.Filled.Person,
                        title = "Resident",
                        subtitle = "Book & view events",
                        isSelected = selectedRole == UserRole.RESIDENT,
                        onClick = { selectedRole = UserRole.RESIDENT },
                        modifier = Modifier.weight(1f)
                    )
                    SignUpRoleCard(
                        icon = Icons.Filled.AdminPanelSettings,
                        title = "Admin",
                        subtitle = "Manage all bookings",
                        isSelected = selectedRole == UserRole.ADMIN,
                        onClick = { selectedRole = UserRole.ADMIN },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Password
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "SET PASSWORD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.8f),
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorMsg = "" },
                    placeholder = { Text("Min. 6 characters", color = TextSecondary) },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility,
                                null, tint = TextSecondary
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.White
                    )
                )
            }

            // Confirm Password
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "CONFIRM PASSWORD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.8f),
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; errorMsg = "" },
                    placeholder = { Text("Re-enter password", color = TextSecondary) },
                    visualTransformation = if (confirmPasswordVisible)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                if (confirmPasswordVisible) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility,
                                null, tint = TextSecondary
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.White
                    )
                )
            }

            // Error message
            if (errorMsg.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.Red.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "⚠️ $errorMsg",
                        modifier = Modifier.padding(12.dp),
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }

            // Success message
            if (successMsg.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF22C55E).copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "✅ $successMsg",
                        modifier = Modifier.padding(12.dp),
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }

            // Sign Up button
            Button(
                onClick = {
                    when {
                        fullName.isBlank() ->
                            errorMsg = "Please enter your full name"
                        email.isBlank() || !email.contains("@") ->
                            errorMsg = "Please enter a valid email"
                        password.length < 6 ->
                            errorMsg = "Password must be at least 6 characters"
                        password != confirmPassword ->
                            errorMsg = "Passwords do not match"
                        else -> {
                            when (val result = viewModel.registerUser(
                                fullName, email, selectedRole, password
                            )) {
                                is AuthResult.Success -> {
                                    successMsg = "Account created! Redirecting to sign in..."
                                    onSignUpSuccess()
                                }
                                is AuthResult.Error -> errorMsg = result.message
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    "CREATE ACCOUNT",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = PrimaryIndigo
                )
            }

            // Sign In link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Already have an account? ",
                    color = Color.White.copy(0.8f),
                    fontSize = 14.sp
                )
                Text(
                    "Sign In",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavigateToSignIn() }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun SignUpField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(0.8f),
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextSecondary) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.White
            )
        )
    }
}

@Composable
fun SignUpRoleCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.White else Color.White.copy(0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                icon, null,
                tint = if (isSelected) PrimaryIndigo else Color.White,
                modifier = Modifier.size(32.dp)
            )
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isSelected) PrimaryIndigo else Color.White
            )
            Text(
                subtitle,
                fontSize = 10.sp,
                color = if (isSelected) TextSecondary else Color.White.copy(0.7f)
            )
        }
    }
}