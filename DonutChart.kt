package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberYellow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DonutChart(
    title: String = "Distribution par Catégorie",
    subtitle: String = "Répartition financière",
    centerTotalText: String = "6 320\nDZD",
    slices: List<DonutSlice> = listOf(
        DonutSlice("1", "Alimentation", "1 896 DZD", 30, NeonPurple),
        DonutSlice("2", "Transport", "1 264 DZD", 20, NeonBlue),
        DonutSlice("3", "Logement", "948 DZD", 15, MintGreen),
        DonutSlice("4", "Loisirs", "948 DZD", 15, AmberYellow),
        DonutSlice("5", "Santé", "632 DZD", 10, CoralRed),
        DonutSlice("6", "Autres", "632 DZD", 10, PinkAccent)
    ),
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var selectedSliceId by remember { mutableStateOf<String?>(null) }
    var animationTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationTriggered = true
    }

    val animProgress by animateFloatAsState(
        targetValue = if (animationTriggered) 1f else 0f,
        animationSpec = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
        label = "donut_smooth_entry"
    )

    val isLight = com.example.ui.theme.isLightMode
    val resolvedGradient = if (isLight) {
        listOf(com.example.ui.theme.NavyCard, com.example.ui.theme.NavyCard)
    } else {
        listOf(
            Color(0xFF1E1346).copy(alpha = 0.75f),
            Color(0xFF0C1026).copy(alpha = 0.88f),
            Color(0xFF070916).copy(alpha = 0.95f)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Brush.linearGradient(colors = resolvedGradient))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.30f),
                        NeonCyan.copy(alpha = 0.20f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(22.dp)
            .testTag("component_donut_chart")
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NeonPurple.copy(alpha = 0.2f))
                            .border(1.dp, NeonPurple.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PieChart,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = title,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = subtitle,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Chart + Legend Layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Glass Donut Chart Canvas
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(156.dp)
                ) {
                    Canvas(modifier = Modifier.size(156.dp)) {
                        val strokeWidth = 38f
                        val radius = (size.minDimension - strokeWidth) / 2
                        val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                        val canvasSize = Size(radius * 2, radius * 2)

                        var currentStartAngle = -90f
                        slices.forEach { slice ->
                            val sweep = (slice.percentage * 3.6f) * animProgress
                            val isSelected = selectedSliceId == slice.id
                            val extraStroke = if (isSelected) 10f else 0f

                            if (sweep > 0f) {
                                drawArc(
                                    color = slice.color,
                                    startAngle = currentStartAngle,
                                    sweepAngle = sweep.coerceAtLeast(0.1f) - if (slices.size > 1) 3f else 0f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = canvasSize,
                                    style = Stroke(width = strokeWidth + extraStroke, cap = StrokeCap.Round)
                                )
                            }
                            currentStartAngle += sweep
                        }
                    }

                    // Center Glass Core
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(92.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = if (isLight) {
                                        listOf(com.example.ui.theme.NavyDark, com.example.ui.theme.NavyCard)
                                    } else {
                                        listOf(Color(0xFF131836).copy(alpha = 0.9f), Color(0xFF080C1E))
                                    }
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Text(
                            text = centerTotalText,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                // Right: Categories List
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    slices.forEach { slice ->
                        val isSelected = selectedSliceId == slice.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) slice.color.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.03f))
                                .border(
                                    width = if (isSelected) 1.dp else 0.dp,
                                    color = if (isSelected) slice.color else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    selectedSliceId = if (selectedSliceId == slice.id) null else slice.id
                                }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(slice.color)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = getCategoryTranslation(slice.label),
                                    color = if (isSelected) Color.White else TextPrimary.copy(alpha = 0.9f),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${slice.percentage}%",
                                color = if (isSelected) NeonCyan else TextPrimary.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getCategoryTranslation(cat: String): String {
    val key = when (cat) {
        "Alimentation" -> "cat_alimentation"
        "Transport" -> "cat_transport"
        "Logement" -> "cat_logement"
        "Loisirs" -> "cat_loisirs"
        "Santé" -> "cat_sante"
        "Autres" -> "cat_autres"
        "Revenus" -> "cat_revenus"
        "Investissements" -> "cat_investissements"
        "Freelance" -> "cat_freelance"
        "Cadeaux" -> "cat_cadeaux"
        else -> ""
    }
    return if (key.isNotEmpty()) com.example.ui.stringRes(key) else cat
}
