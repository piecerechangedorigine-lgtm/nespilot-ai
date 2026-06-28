package com.example.ui.components

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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ui.stringRes

import androidx.compose.ui.unit.sp
import com.example.data.model.Transaction
import com.example.ui.theme.AmberYellow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.DecimalFormat

@Composable
fun RecentTransactionsSection(
    transactions: List<Transaction>,
    onAddClick: () -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        transactions.take(4).forEach { tx ->
            SingleTransactionRow(
                transaction = tx,
                onClick = { onTransactionClick(tx) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Floating Quick Add Action inside Recent list area matching reference layout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier
                    .size(56.dp)
                    .testTag("fab_add_transaction"),
                shape = CircleShape,
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(NeonBlue, NeonPurple)
                            )
                        )
                        .border(1.dp, GlassBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringRes("add_transaction"),
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SingleTransactionRow(
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

    val iconBgColor = when (transaction.category) {
        "Alimentation" -> NeonPurple
        "Transport" -> NeonBlue
        "Logement" -> MintGreen
        "Loisirs" -> AmberYellow
        "Santé" -> CoralRed
        "Revenus" -> MintGreen
        else -> PinkAccent
    }

    val relativeDateStr = when {
        System.currentTimeMillis() - transaction.dateMillis < 86400000L -> stringRes("today")
        System.currentTimeMillis() - transaction.dateMillis < 172800000L -> stringRes("yesterday")
        else -> stringRes("days_ago")
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                run {
                    val isRtl = androidx.compose.ui.platform.LocalLayoutDirection.current == androidx.compose.ui.unit.LayoutDirection.Rtl
                    val gradientColors = if (isRtl) {
                        listOf(
                            Color(0xFF0C132B).copy(alpha = 0.3f),
                            Color(0xFF0C132B).copy(alpha = 0.5f)
                        )
                    } else {
                        listOf(
                            Color(0xFF0C132B).copy(alpha = 0.5f), 
                            Color(0xFF0C132B).copy(alpha = 0.3f)
                        )
                    }
                    Brush.horizontalGradient(
                        colors = gradientColors
                    )
                }
            )
            .border(1.dp, GlassBorder.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                iconBgColor.copy(alpha = 0.6f),
                                iconBgColor.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .border(1.dp, iconBgColor.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = transaction.category,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column {
                Text(
                    text = transaction.title,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                    letterSpacing = (-0.3).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(iconBgColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .border(1.dp, iconBgColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = iconBgColor,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = transaction.category,
                        color = iconBgColor.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$signStr$amountStr",
                color = amountColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = relativeDateStr,
                color = TextSecondary.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        }
    }
}
