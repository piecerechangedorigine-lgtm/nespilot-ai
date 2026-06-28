package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CoralRed
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextSecondary

@Composable
fun StatisticsCards(
    incomeFormatted: String,
    incomePercent: String,
    expensesFormatted: String,
    expensesPercent: String,
    balanceFormatted: String,
    balancePercent: String,
    incomeLabel: String = "Revenus",
    expensesLabel: String = "Dépenses",
    balanceLabel: String = "Solde Total",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top Full-Width Balance Card
        GridGlassCard(
            title = balanceLabel,
            amount = balanceFormatted,
            percent = balancePercent,
            icon = Icons.Default.AccountBalanceWallet,
            accentColor = NeonCyan,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_stat_balance")
        )

        // Bottom 2-Column Grid Row for Income and Expenses
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GridGlassCard(
                title = incomeLabel,
                amount = incomeFormatted,
                percent = incomePercent,
                icon = Icons.Default.ArrowDownward,
                accentColor = MintGreen,
                modifier = Modifier
                    .weight(1f)
                    .testTag("card_stat_income")
            )

            GridGlassCard(
                title = expensesLabel,
                amount = expensesFormatted,
                percent = expensesPercent,
                icon = Icons.Default.ArrowUpward,
                accentColor = CoralRed,
                modifier = Modifier
                    .weight(1f)
                    .testTag("card_stat_expenses")
            )
        }
    }
}

@Composable
private fun GridGlassCard(
    title: String,
    amount: String,
    percent: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val isLight = com.example.ui.theme.isLightMode
    val resolvedGradient = if (isLight) {
        listOf(accentColor.copy(alpha = 0.08f), com.example.ui.theme.NavyCard)
    } else {
        listOf(accentColor.copy(alpha = 0.15f), com.example.ui.theme.NavyCard)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(resolvedGradient))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.25f), Color.White.copy(alpha = 0.05f))
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = title,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = percent,
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = amount,
                color = com.example.ui.theme.TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
