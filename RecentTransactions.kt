package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Transaction
import com.example.ui.theme.AmberYellow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.DecimalFormat

@Composable
fun RecentTransactions(
    transactions: List<Transaction>,
    onAddClick: () -> Unit = {},
    onTransactionClick: (Transaction) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var animationTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationTriggered = true
    }

    val alphaAnim by animateFloatAsState(
        targetValue = if (animationTriggered) 1f else 0f,
        animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
        label = "recent_tx_fade"
    )

    val isLight = com.example.ui.theme.isLightMode
    val resolvedGradient = if (isLight) {
        listOf(com.example.ui.theme.NavyCard, com.example.ui.theme.NavyCard)
    } else {
        listOf(
            Color(0xFF131A40).copy(alpha = 0.75f),
            Color(0xFF0A0F26).copy(alpha = 0.88f),
            Color(0xFF060816).copy(alpha = 0.95f)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alphaAnim)
            .clip(RoundedCornerShape(26.dp))
            .background(Brush.linearGradient(colors = resolvedGradient))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.30f),
                        NeonBlue.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(20.dp)
            .testTag("component_recent_transactions")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header inside glass card
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
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(NeonCyan)
                    )
                    Text(
                        text = com.example.ui.stringRes("financial_activity"),
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = com.example.ui.stringRes("operations").format(transactions.size),
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Scrollable / Staggered Items List
            if (transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = com.example.ui.stringRes("no_recent_transactions"),
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            } else {
                transactions.take(5).forEach { tx ->
                    GlassTransactionRow(
                        transaction = tx,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onTransactionClick(tx)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Bottom Quick Add Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = com.example.ui.stringRes("real_time_update"),
                    color = TextSecondary.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )

                FloatingActionButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAddClick()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("fab_add_recent_transaction"),
                    shape = CircleShape,
                    containerColor = Color.Transparent,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(NeonBlue, NeonPurple)
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ajouter une transaction",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassTransactionRow(
    transaction: Transaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DecimalFormat("#,##0.00")
    val amountStr = formatter.format(transaction.amount) + " DZD"
    val signStr = if (transaction.isIncome) "+" else "-"
    val amountColor = if (transaction.isIncome) MintGreen else CoralRed

    val iconVector = when (transaction.iconName) {
        "ShoppingCart" -> Icons.Default.ShoppingCart
        "MusicNote" -> Icons.Default.MusicNote
        "DirectionsCar" -> Icons.Default.DirectionsCar
        "LocalHospital" -> Icons.Default.LocalHospital
        "Home" -> Icons.Default.Home
        "AccountBalanceWallet" -> Icons.Default.AccountBalanceWallet
        else -> Icons.Default.LocalCafe
    }

    val iconAccentColor = when (transaction.category) {
        "Alimentation" -> NeonPurple
        "Transport" -> NeonBlue
        "Logement" -> MintGreen
        "Loisirs" -> AmberYellow
        "Santé" -> CoralRed
        "Revenus" -> MintGreen
        else -> PinkAccent
    }

    val relativeDateStr = when {
        System.currentTimeMillis() - transaction.dateMillis < 86400000L -> com.example.ui.stringRes("today")
        System.currentTimeMillis() - transaction.dateMillis < 172800000L -> com.example.ui.stringRes("yesterday")
        else -> com.example.ui.stringRes("days_ago")
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(com.example.ui.theme.NavyCard)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.18f),
                        iconAccentColor.copy(alpha = 0.20f),
                        Color.White.copy(alpha = 0.03f)
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconAccentColor.copy(alpha = 0.18f))
                    .border(1.dp, iconAccentColor.copy(alpha = 0.45f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = transaction.category,
                    tint = iconAccentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = transaction.title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = getCategoryTranslation(transaction.category),
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(TextSecondary.copy(alpha = 0.5f))
                    )
                    Text(
                        text = relativeDateStr,
                        color = TextSecondary.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = if (transaction.isIncome) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = amountColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "$signStr$amountStr",
                color = amountColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )
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
