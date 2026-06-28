package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple

@Composable
fun AuroraBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora_transition")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.16f,
        targetValue = 0.30f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aurora_alpha"
    )

    val offsetFactor by infiniteTransition.animateFloat(
        initialValue = 0.18f,
        targetValue = 0.32f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aurora_offset"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDark)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Top-Right Purple Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonPurple.copy(alpha = pulseAlpha), NeonPurple.copy(alpha = 0f)),
                    center = Offset(w * 0.85f, h * offsetFactor),
                    radius = w * 0.75f
                ),
                center = Offset(w * 0.85f, h * offsetFactor),
                radius = w * 0.75f
            )

            // Top-Left Cyan Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonCyan.copy(alpha = pulseAlpha * 0.65f), NeonCyan.copy(alpha = 0f)),
                    center = Offset(w * 0.15f, h * 0.15f),
                    radius = w * 0.65f
                ),
                center = Offset(w * 0.15f, h * 0.15f),
                radius = w * 0.65f
            )

            // Middle Blue Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonBlue.copy(alpha = pulseAlpha * 0.5f), NeonBlue.copy(alpha = 0f)),
                    center = Offset(w * 0.5f, h * 0.55f),
                    radius = w * 0.8f
                ),
                center = Offset(w * 0.5f, h * 0.55f),
                radius = w * 0.8f
            )
        }

        content()
    }
}
