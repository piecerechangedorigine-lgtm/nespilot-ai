package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = if (isLightMode) {
        lightColorScheme(
            primary = NeonPurple,
            onPrimary = Color.White,
            secondary = NeonCyan,
            onSecondary = NavyDark,
            tertiary = MintGreen,
            background = NavyDark,
            onBackground = TextPrimary,
            surface = NavyCard,
            onSurface = TextPrimary,
            surfaceVariant = NavyElevated,
            onSurfaceVariant = TextSecondary,
            error = CoralRed,
            onError = Color.White,
            outline = GlassBorder
        )
    } else {
        darkColorScheme(
            primary = NeonPurple,
            onPrimary = Color.White,
            secondary = NeonCyan,
            onSecondary = NavyDark,
            tertiary = MintGreen,
            background = NavyDark,
            onBackground = TextPrimary,
            surface = NavyCard,
            onSurface = TextPrimary,
            surfaceVariant = NavyElevated,
            onSurfaceVariant = TextSecondary,
            error = CoralRed,
            onError = Color.White,
            outline = GlassBorder
        )
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isLightMode
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = isLightMode
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

