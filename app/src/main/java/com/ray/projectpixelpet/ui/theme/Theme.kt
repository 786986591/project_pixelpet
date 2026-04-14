package com.ray.projectpixelpet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF2A2318),
    onPrimary = Color(0xFFFFF9F0),
    primaryContainer = Color(0xFFFFD898),
    secondary = Color(0xFF7BC8A4),
    tertiary = Color(0xFF75BFFF),
    background = Color(0xFFFFFBF4),
    surface = Color(0xFFFFF4D6),
    surfaceVariant = Color(0xFFFFE9B8)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFE6A8),
    onPrimary = Color(0xFF2A2318),
    primaryContainer = Color(0xFF5A4320),
    secondary = Color(0xFF7BC8A4),
    tertiary = Color(0xFF75BFFF),
    background = Color(0xFF17120B),
    surface = Color(0xFF241C12),
    surfaceVariant = Color(0xFF2E2418)
)

@Composable
fun ProjectPixelPetTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}

