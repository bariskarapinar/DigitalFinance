package com.apexvest.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ApexVestColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = NeonPurple,
    tertiary = NeonPink,
    background = DeepBlack,
    surface = GlassBlack,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun FinanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // ApexVest is permanently in Dark Mode for maximum eye-catchiness
    MaterialTheme(
        colorScheme = ApexVestColorScheme,
        content = content
    )
}
