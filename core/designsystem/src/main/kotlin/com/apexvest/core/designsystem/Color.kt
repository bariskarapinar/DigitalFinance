package com.apexvest.core.designsystem

import androidx.compose.ui.graphics.Color

// ApexVest Neon Palette
val NeonCyan = Color(0xFF00FBFF)
val NeonPurple = Color(0xFFBC13FE)
val NeonPink = Color(0xFFFF00BD)
val NeonGreen = Color(0xFF00FF41)
val DeepBlack = Color(0xFF0A0A0A)
val GlassWhite = Color(0x1AFFFFFF)
val GlassBlack = Color(0x33000000)

// Vibrant Stock Colors
val BullishGreen = Color(0xFF00FF9D)
val BearishRed = Color(0xFFFF2E63)

// Gradient Sets
val PrimaryGradient = listOf(NeonCyan, NeonPurple)
val SecondaryGradient = listOf(NeonPurple, NeonPink)
val BullishGradient = listOf(BullishGreen.copy(alpha = 0.5f), Color.Transparent)
val BearishGradient = listOf(BearishRed.copy(alpha = 0.5f), Color.Transparent)
