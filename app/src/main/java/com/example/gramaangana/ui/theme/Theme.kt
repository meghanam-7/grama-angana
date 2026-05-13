package com.example.gramaangana.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryIndigo,
    onPrimary = Color.White,
    primaryContainer = PrimaryIndigoLight,
    secondary = PrimaryIndigoLight,
    background = Color(0xFFF0F2F8),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFEEF0F8),
    onBackground = Color(0xFF1E1B4B),
    onSurface = Color(0xFF1E1B4B),
    onSurfaceVariant = Color(0xFF6B7280),
    outline = Color(0xFFE0E0E0),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF818CF8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3730A3),
    secondary = Color(0xFF818CF8),
    background = Color(0xFF111318),
    surface = Color(0xFF1E2028),
    surfaceVariant = Color(0xFF2A2D38),
    onBackground = Color(0xFFE8E9F0),
    onSurface = Color(0xFFE8E9F0),
    onSurfaceVariant = Color(0xFFAAADB8),
    outline = Color(0xFF3A3D48),
)

@Composable
fun GramaAnganaTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}