package com.example.ui.components

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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberYellow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class DonutSlice(
    val id: String,
    val label: String,
    val amountFormatted: String,
    val percentage: Int,
    val color: Color
)

@Composable
fun DonutChartCard(
    title: String = "Dépenses par catégorie",
    subtitle: String = "Ce mois-ci",
    centerTotalText: String = "6 320,00\nDZD",
    slices: List<DonutSlice> = listOf(
        DonutSlice("1", "Alimentation", "1 896,00 DZD", 30, NeonPurple),
        DonutSlice("2", "Transport", "1 264,00 DZD", 20, NeonBlue),
        DonutSlice("3", "Logement", "948,00 DZD", 15, MintGreen),
        DonutSlice("4", "Loisirs", "948,00 DZD", 15, AmberYellow),
        DonutSlice("5", "Santé", "632,00 DZD", 10, CoralRed),
        DonutSlice("6", "Autres", "632,00 DZD", 10, PinkAccent)
    ),
    modifier: Modifier = Modifier
) {
    var selectedSliceId by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("donut_chart_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F1736).copy(alpha = 0.85f), Color(0xFF070B18).copy(alpha = 0.95f))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                // Title Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Options",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Interactive Donut Chart with Center Text
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(150.dp)
                    ) {
                        // Drawing Donut
                        val animProgress by animateFloatAsState(targetValue = 1f, animationSpec = tween(1200), label = "donut_anim")
                        Canvas(modifier = Modifier.size(150.dp)) {
                            val strokeWidth = 36f
                            val radius = (size.minDimension - strokeWidth) / 2
                            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                            val canvasSize = Size(radius * 2, radius * 2)

                            var currentStartAngle = -90f
                            slices.forEach { slice ->
                                val sweep = (slice.percentage * 3.6f) * animProgress
                                val isSelected = selectedSliceId == slice.id
                                val extraStroke = if (isSelected) 8f else 0f

                                drawArc(
                                    color = slice.color,
                                    startAngle = currentStartAngle,
                                    sweepAngle = sweep - 2f, // gap between slices
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = canvasSize,
                                    style = Stroke(width = strokeWidth + extraStroke, cap = StrokeCap.Round)
                                )
                                currentStartAngle += sweep
                            }
                        }

                        // Center Hole Text
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF090E22))
                                .border(1.dp, GlassBorder, CircleShape)
                        ) {
                            Text(
                                text = centerTotalText,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Right: Category Legend
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        slices.forEach { slice ->
                            val isSelected = selectedSliceId == slice.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) slice.color.copy(alpha = 0.15f) else Color.Transparent)
                                    .clickable {
                                        selectedSliceId = if (selectedSliceId == slice.id) null else slice.id
                                    }
                                    .padding(horizontal = 4.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(slice.color)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = slice.label,
                                        color = if (isSelected) Color.White else TextPrimary.copy(alpha = 0.9f),
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Text(
                                    text = slice.amountFormatted,
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${slice.percentage}%",
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
