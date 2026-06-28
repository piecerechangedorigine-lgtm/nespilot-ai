package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Transaction
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SpendingLineChartCard(
    transactions: List<Transaction>,
    modifier: Modifier = Modifier
) {
    // Filter expenses and sort by date
    val expenses = transactions.filter { !it.isIncome }.sortedBy { it.dateMillis }
    
    // Group by day to get daily spending
    val dailySpending = expenses.groupBy { 
        val calendar = Calendar.getInstance().apply { timeInMillis = it.dateMillis }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.timeInMillis
    }.mapValues { entry -> 
        entry.value.sumOf { it.amount }
    }.toSortedMap()

    val dataPoints = dailySpending.values.toList()
    val labels = dailySpending.keys.map { 
        SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(it)) 
    }

    val isLight = com.example.ui.theme.isLightMode
    val resolvedGradient = if (isLight) {
        listOf(com.example.ui.theme.NavyCard, com.example.ui.theme.NavyCard)
    } else {
        listOf(Color(0xFF1E1346).copy(alpha = 0.75f), Color(0xFF0C102A).copy(alpha = 0.85f))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(resolvedGradient))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.25f), Color.White.copy(alpha = 0.05f))
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = com.example.ui.stringRes("spending_trend"),
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            if (dataPoints.size > 1) {
                LineChartCanvas(
                    data = dataPoints.map { it.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (labels.isNotEmpty()) {
                        Text(labels.first(), color = TextSecondary, fontSize = 10.sp)
                        if (labels.size > 2) {
                            Text(labels[labels.size / 2], color = TextSecondary, fontSize = 10.sp)
                        }
                        Text(labels.last(), color = TextSecondary, fontSize = 10.sp)
                    }
                }
            } else {
                Text(
                    text = com.example.ui.stringRes("no_chart_data"),
                    color = TextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 40.dp)
                )
            }
        }
    }
}

@Composable
fun LineChartCanvas(data: List<Float>, modifier: Modifier = Modifier) {
    val maxData = data.maxOrNull() ?: 1f
    val minData = data.minOrNull() ?: 0f
    val range = (maxData - minData).coerceAtLeast(1f)
    
    var animationTriggered by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        animationTriggered = true
    }

    val animProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (animationTriggered) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 1200, easing = androidx.compose.animation.core.FastOutSlowInEasing),
        label = "line_chart_smooth_entry"
    )
    
    val isRtl = androidx.compose.ui.platform.LocalLayoutDirection.current == androidx.compose.ui.unit.LayoutDirection.Rtl
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val pointSpacing = width / (data.size - 1).coerceAtLeast(1)
        
        val path = Path()
        val fillPath = Path()
        
        var prevX = if (isRtl) width else 0f
        var prevY = height
        
        data.forEachIndexed { index, value ->
            val normalizedValue = (value - minData) / range
            val targetY = height - (normalizedValue * height * 0.8f) // 0.8f leaves some top padding
            val y = height - ((height - targetY) * animProgress)
            val x = if (isRtl) width - (index * pointSpacing) else index * pointSpacing
            
            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                val cpX = (prevX + x) / 2f
                path.cubicTo(cpX, prevY, cpX, y, x, y)
                fillPath.cubicTo(cpX, prevY, cpX, y, x, y)
            }
            prevX = x
            prevY = y
        }
        
        fillPath.lineTo(width, height)
        fillPath.close()
        
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(NeonCyan.copy(alpha = 0.3f * animProgress), Color.Transparent)
            )
        )
        
        drawPath(
            path = path,
            color = NeonCyan.copy(alpha = animProgress),
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
        
        data.forEachIndexed { index, value ->
            val normalizedValue = (value - minData) / range
            val targetY = height - (normalizedValue * height * 0.8f)
            val y = height - ((height - targetY) * animProgress)
            val x = if (isRtl) width - (index * pointSpacing) else index * pointSpacing
            drawCircle(
                color = NeonPurple.copy(alpha = animProgress),
                radius = 5.dp.toPx(),
                center = Offset(x, y)
            )
            drawCircle(
                color = Color.White.copy(alpha = animProgress),
                radius = 3.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}
