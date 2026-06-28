package com.example.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

var isLightMode by mutableStateOf(false)

val NavyDark: Color get() = if (isLightMode) Color(0xFFF0F4F8) else Color(0xFF070B18)
val NavyCard: Color get() = if (isLightMode) Color(0xFFFFFFFF) else Color(0xFF0D142B)
val NavyElevated: Color get() = if (isLightMode) Color(0xFFE2E8F0) else Color(0xFF151F42)
val NeonPurple: Color get() = if (isLightMode) Color(0xFF5A52D0) else Color(0xFF6C63FF)
val NeonPurpleLight: Color get() = if (isLightMode) Color(0xFF7A73E0) else Color(0xFF8B83FF)
val NeonCyan: Color get() = if (isLightMode) Color(0xFF0096A6) else Color(0xFF00E5FF)
val NeonBlue: Color get() = if (isLightMode) Color(0xFF2563EB) else Color(0xFF3B82F6)
val MintGreen: Color get() = if (isLightMode) Color(0xFF16A34A) else Color(0xFF22C55E)
val CoralRed: Color get() = if (isLightMode) Color(0xFFDC2626) else Color(0xFFEF4444)
val AmberYellow: Color get() = if (isLightMode) Color(0xFFD97706) else Color(0xFFF59E0B)
val PinkAccent: Color get() = if (isLightMode) Color(0xFFDB2777) else Color(0xFFEC4899)

val GlassBorder: Color get() = if (isLightMode) Color(0x33000000) else Color(0x1EFFFFFF)
val GlassBg: Color get() = if (isLightMode) Color(0x0A000000) else Color(0x12FFFFFF)
val TextPrimary: Color get() = if (isLightMode) Color(0xFF111827) else Color(0xFFFFFFFF)
val TextSecondary: Color get() = if (isLightMode) Color(0xFF4B5563) else Color(0xB3FFFFFF)
val TextTertiary: Color get() = if (isLightMode) Color(0xFF9CA3AF) else Color(0x73FFFFFF)

