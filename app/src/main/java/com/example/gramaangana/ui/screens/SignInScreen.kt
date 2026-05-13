package com.example.gramaangana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramaangana.AppViewModel
import com.example.gramaangana.AuthResult
import com.example.gramaangana.ui.theme.*

@Composable
fun SignInScreen(
    viewModel: AppViewModel,
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(60.dp))

            // Logo area
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(0.2f),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🏛️", fontSize = 36.sp)
                }
            }

            Text(
                "Welcome Back!",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                "Sign in to Grama Angana",
                fontSize = 14.sp,
                color = Color.White.copy(0.8f)
            )

            Spacer(Modifier.height(8.dp))

            // Email field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "EMAIL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.8f),
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; errorMsg = "" },
                    placeholder = { Text("your@email.com", color = TextSecondary) },
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

            // Password field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "PASSWORD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.8f),
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorMsg = "" },
                    placeholder = { Text("Enter password", color = TextSecondary) },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility,
                                null,
                                tint = TextSecondary
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

            // Sign In button
            Button(
                onClick = {
                    when {
                        email.isBlank() -> errorMsg = "Please enter your email"
                        password.isBlank() -> errorMsg = "Please enter your password"
                        else -> {
                            when (val result = viewModel.signIn(email, password)) {
                                is AuthResult.Success -> onSignInSuccess()
                                is AuthResult.Error -> errorMsg = result.message
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(
                    "SIGN IN",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = PrimaryIndigo
                )
            }

            // Sign Up link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Don't have an account? ",
                    color = Color.White.copy(0.8f),
                    fontSize = 14.sp
                )
                Text(
                    "Sign Up",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}