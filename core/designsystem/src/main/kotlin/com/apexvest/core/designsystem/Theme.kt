package com.apexvest.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGold,
    secondary = GreenStock,
    tertiary = RedStock,
    background = DarkBackground,
    surface = SurfaceGray,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextWhite,
    onSurface = TextWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGold,
    secondary = GreenStock,
    tertiary = RedStock,
    // Add light colors if needed, but the app is dark by default
)

@Composable
fun FinanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
