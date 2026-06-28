package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CoralRed
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyElevated
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.stringRes
import com.example.ui.I18n
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsMoreScreen(
    currentLanguage: String,
    isPro: Boolean,
    onLanguageChange: (String) -> Unit,
    onTogglePro: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        Text(
            text = stringRes("settings_title"),
            color = TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringRes("settings_subtitle"),
            color = TextSecondary,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Profile VIP Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = if (isPro) listOf(Color(0xFF3B1F7E), Color(0xFF1E3A8A))
                        else listOf(NavyCard, NavyElevated)
                    )
                )
                .border(1.dp, if (isPro) NeonCyan else GlassBorder, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(NeonPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(36.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Yacine Ben", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isPro) NeonCyan else Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isPro) "✦ PRO" else "FREE",
                                color = if (isPro) NavyDark else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("yacine.ben@fintech.ai", color = TextSecondary, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Section: Abonnement VIP Toggle
        Text(stringRes("pro_subscription"), color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NavyCard)
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonPurple.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.WorkspacePremium, null, tint = NeonPurple)
                    }
                    Column {
                        Text(stringRes("pro_title"), color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text(if (isPro) stringRes("pro_desc_on") else stringRes("pro_desc_off"), color = TextSecondary, fontSize = 12.sp)
                    }
                }
                Switch(
                    checked = isPro,
                    onCheckedChange = { onTogglePro() },
                    colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = NeonPurple),
                    modifier = Modifier.testTag("toggle_subscription_pro")
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Langues (i18n: Arabic / French / English)
        Text(stringRes("lang_title"), color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val languages = listOf("FR" to "Français 🇫🇷", "EN" to "English 🇺🇸", "AR" to "العربية 🇩🇿")
            languages.forEach { (code, label) ->
                val isSel = currentLanguage == code
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSel) NeonBlue else NavyCard)
                        .border(1.dp, if (isSel) NeonCyan else GlassBorder, RoundedCornerShape(14.dp))
                        .clickable { 
                            onLanguageChange(code)
                            I18n.changeLanguage(code) 
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = Color.White, fontSize = 12.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Section: Autre Paramètres
        Text(stringRes("general_settings"), color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NavyCard)
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
        ) {
            SettingsOptionRow(stringRes("currency"), stringRes("currency_val"), Icons.Default.Language)
            Box(Modifier.fillMaxWidth().height(1.dp).background(GlassBorder))
            SettingsOptionRow(stringRes("security"), stringRes("security_val"), Icons.Default.Security)
            Box(Modifier.fillMaxWidth().height(1.dp).background(GlassBorder))
            SettingsOptionRow(stringRes("about"), stringRes("about_val"), Icons.Default.Info)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Logout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CoralRed.copy(alpha = 0.15f))
                .border(1.dp, CoralRed.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                .clickable { }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Logout, null, tint = CoralRed, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(stringRes("logout"), color = CoralRed, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun SettingsOptionRow(title: String, subtitle: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(NavyElevated), Alignment.Center) {
                Icon(icon, null, tint = TextPrimary, modifier = Modifier.size(18.dp))
            }
            Column {
                Text(title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(subtitle, color = TextSecondary, fontSize = 12.sp)
            }
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = TextSecondary)
    }
}
