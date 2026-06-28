package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.FinanceRepository
import com.example.ui.components.AddGoalModal
import com.example.ui.components.AddTransactionModal
import com.example.ui.components.AiChatScreen
import com.example.ui.components.AuroraBackground
import com.example.ui.components.BudgetsTabScreen
import com.example.ui.components.DailyAdviceModal
import com.example.ui.components.DonutChart
import com.example.ui.components.DonutChartCard
import com.example.ui.components.DonutSlice
import com.example.ui.components.FintechBottomBar
import com.example.ui.components.GeminiChatFab
import com.example.ui.components.HeroCard
import com.example.ui.components.QuickOverviewHeader
import com.example.ui.components.RecentTransactions
import com.example.ui.components.SavingsGoalCard
import com.example.ui.components.SettingsMoreScreen
import com.example.ui.components.StatisticsCards
import com.example.ui.components.TransactionsTabScreen
import com.example.ui.stringRes
import com.example.ui.theme.AmberYellow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MintGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FinanceViewModel
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.example.ui.I18n.init(this)
        setContent {
            MyApplicationTheme {
                val currentLang by com.example.ui.I18n.currentLanguage.collectAsState()
                val layoutDirection = if (currentLang == "AR") androidx.compose.ui.unit.LayoutDirection.Rtl else androidx.compose.ui.unit.LayoutDirection.Ltr
                androidx.compose.runtime.CompositionLocalProvider(androidx.compose.ui.platform.LocalLayoutDirection provides layoutDirection) {
                    val context = LocalContext.current
                    val db = remember { AppDatabase.getDatabase(context) }
                    val repo = remember { FinanceRepository(db.financeDao()) }
                    val factory = remember { FinanceViewModel.Factory(repo) }
                    val viewModel: FinanceViewModel = viewModel(factory = factory)

                    FintechMainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun FintechMainScreen(viewModel: FinanceViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val goals by viewModel.goals.collectAsState()
    val budgets by viewModel.budgets.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val dailyAdviceVisible by viewModel.dailyAdviceVisible.collectAsState()
    val dailyAdviceText by viewModel.dailyAdviceText.collectAsState()
    val isLoadingAdvice by viewModel.isLoadingAdvice.collectAsState()
    val addTxVisible by viewModel.addTxVisible.collectAsState()
    val isSendingChat by viewModel.isSendingChat.collectAsState()
    val language by viewModel.language.collectAsState()
    val isPro by viewModel.isPro.collectAsState()
    
    var showGoalsScreen by remember { androidx.compose.runtime.mutableStateOf(false) }
    var addGoalVisible by remember { androidx.compose.runtime.mutableStateOf(false) }
    var searchQuery by remember { androidx.compose.runtime.mutableStateOf("") }

    val filteredTransactions = transactions.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
        it.category.contains(searchQuery, ignoreCase = true) ||
        it.amount.toString().contains(searchQuery, ignoreCase = true)
    }

        val greetingPrefix = stringRes("greeting")

    AuroraBackground {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
        topBar = {
            FintechTopAppBar(
                tagline = when (language) {
                    "EN" -> "Your AI Finance Copilot"
                    "AR" -> "مساعدك المالي الذكي"
                    else -> "Your AI Finance Copilot"
                },
                onNotificationClick = { viewModel.showDailyAdvice() },
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        },
        bottomBar = {
            FintechBottomBar(
                selectedIndex = selectedTab,
                onTabSelect = { viewModel.selectTab(it) }
            )
        },
        floatingActionButton = {
            if (selectedTab == 0) {
                GeminiChatFab(onClick = { viewModel.selectTab(2) })
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (showGoalsScreen) {
                com.example.ui.components.SavingsGoalsScreen(
                    goals = goals,
                    onAddGoalClick = { addGoalVisible = true },
                    onDeleteGoal = { viewModel.deleteGoal(it) },
                    onBack = { showGoalsScreen = false }
                )
            } else {
                when (selectedTab) {
                    0 -> { // Dashboard Accueil
                        DashboardScrollView(
                            greetingPrefix = greetingPrefix,
                            userName = "Yacine",
                            onAdviceClick = { viewModel.showDailyAdvice() },
                            onSeeAllClick = { viewModel.selectTab(it) },
                            onAddTransactionClick = { viewModel.openAddTransaction() },
                            onGoalsClick = { showGoalsScreen = true },
                            transactions = filteredTransactions,
                            goals = goals,
                            budgets = budgets
                        )
                    }
                1 -> { // Transactions Tab
                    TransactionsTabScreen(
                        transactions = filteredTransactions,
                        onAddClick = { viewModel.openAddTransaction() }
                    )
                }
                2 -> { // AI Copilot Chat
                    AiChatScreen(
                        messages = chatMessages,
                        isSending = isSendingChat,
                        onSendMessage = { viewModel.sendChatMessage(it) },
                        onClearChat = { viewModel.clearChat() }
                    )
                }
                3 -> { // Budgets Tab
                    BudgetsTabScreen(budgets = budgets)
                }
                4 -> { // Settings Plus
                    SettingsMoreScreen(
                        currentLanguage = language,
                        isPro = isPro,
                        onLanguageChange = { viewModel.setLanguage(it) },
                        onTogglePro = { viewModel.togglePro() }
                    )
                }
            }
            } // Close the else block

            // Dialog Modals
            if (dailyAdviceVisible) {
                DailyAdviceModal(
                    adviceText = dailyAdviceText,
                    isLoading = isLoadingAdvice,
                    onDismiss = { viewModel.dismissDailyAdvice() }
                )
            }

            if (addTxVisible) {
                AddTransactionModal(
                    onDismiss = { viewModel.closeAddTransaction() },
                    onSubmit = { title, category, amount, isInc ->
                        viewModel.submitNewTransaction(title, category, amount, isInc)
                    }
                )
            }

            if (addGoalVisible) {
                AddGoalModal(
                    onDismiss = { addGoalVisible = false },
                    onSubmit = { title, amount, emoji, days ->
                        viewModel.addGoal(title, amount, emoji, days)
                        addGoalVisible = false
                    }
                )
            }
        }
    }
}
}

@Composable
fun FintechTopAppBar(
    tagline: String,
    onNotificationClick: () -> Unit,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }) {
                    Icon(Icons.Default.Menu, "Menu", tint = TextPrimary, modifier = Modifier.size(28.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Brush.linearGradient(listOf(NeonPurple, NeonBlue)))
                    ) {
                        Icon(Icons.Default.Psychology, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Column {
                        Row {
                            Text("NESPILOT ", color = TextPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                            Text("AI", color = NeonCyan, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                        }
                        Text(tagline, color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Theme Toggle
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(NavyCard)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            com.example.ui.theme.isLightMode = !com.example.ui.theme.isLightMode
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (com.example.ui.theme.isLightMode) {
                        androidx.compose.material3.Text("🌙", fontSize = 18.sp)
                    } else {
                        androidx.compose.material3.Text("☀️", fontSize = 18.sp)
                    }
                }

                // Notification Bell with Badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(NavyCard)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onNotificationClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Notifications, "Notifications", tint = TextPrimary, modifier = Modifier.size(22.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 4.dp, end = 4.dp)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(NeonPurple)
                    ) {
                        Text("3", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // User Avatar
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(NeonPurple, NeonBlue)))
                        .border(1.5.dp, NeonCyan, CircleShape)
                        .clickable { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
                ) {
                    Icon(Icons.Default.Person, "Avatar", tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }
        }
        
        // Search bar in the header
        androidx.compose.material3.OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(stringRes("search_placeholder"), color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = TextSecondary) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = NavyCard,
                focusedContainerColor = NavyCard.copy(alpha = 0.5f),
                unfocusedContainerColor = NavyCard.copy(alpha = 0.3f),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}

@Composable
fun DashboardScrollView(
    greetingPrefix: String,
    userName: String,
    onAdviceClick: () -> Unit,
    onSeeAllClick: (Int) -> Unit,
    onAddTransactionClick: () -> Unit,
    onGoalsClick: () -> Unit,
    transactions: List<com.example.data.model.Transaction>,
    goals: List<com.example.data.model.SavingsGoal>,
    budgets: List<com.example.data.model.Budget>
) {
    val scrollState = rememberScrollState()
    val formatter = DecimalFormat("#,##0.00")

    val totalIncome = transactions.filter { it.isIncome }.sumOf { it.amount }
    val totalExpenses = transactions.filter { !it.isIncome }.sumOf { it.amount }
    val netBalance = totalIncome - totalExpenses

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. AI Hero Card
        HeroCard(
            userName = userName,
            greetingPrefix = greetingPrefix,
            adviceButtonText = stringRes("advice_btn"),
            summaryText = stringRes("summary_text"),
            onAdviceClick = onAdviceClick
        )

        // 2. Quick Overview Section & Three Statistics Cards
        QuickOverviewHeader(
            title = stringRes("quick_overview"),
            actionText = stringRes("view_all"),
            onActionClick = { onSeeAllClick(1) }
        )

        StatisticsCards(
            incomeFormatted = "${formatter.format(if (totalIncome > 0) totalIncome else 12540.0)} DZD",
            incomePercent = "+12%",
            expensesFormatted = "${formatter.format(if (totalExpenses > 0) totalExpenses else 6320.0)} DZD",
            expensesPercent = "-18%",
            balanceFormatted = "${formatter.format(if (netBalance != 0.0) netBalance else 6220.0)} DZD",
            balancePercent = "+28%",
            incomeLabel = stringRes("income"),
            expensesLabel = stringRes("expense"),
            balanceLabel = stringRes("total_balance")
        )

        // 3. Donut Chart Card (Dépenses par catégorie)
        val expenseTransactions = transactions.filter { !it.isIncome }
        val donutSlices = if (expenseTransactions.isNotEmpty()) {
            val totalExpenseAmt = expenseTransactions.sumOf { it.amount }
            val expensesByCategory = expenseTransactions.groupBy { it.category }.mapValues { entry -> entry.value.sumOf { it.amount } }
            
            expensesByCategory.entries.sortedByDescending { it.value }.mapIndexed { idx, entry ->
                val pct = if (totalExpenseAmt > 0) ((entry.value / totalExpenseAmt) * 100).toInt() else 0
                val c = when(idx % 6) {
                    0 -> NeonPurple
                    1 -> NeonBlue
                    2 -> MintGreen
                    3 -> AmberYellow
                    4 -> CoralRed
                    else -> PinkAccent
                }
                DonutSlice(idx.toString(), entry.key, "${formatter.format(entry.value)} DZD", pct, c)
            }
        } else {
            listOf(
                DonutSlice("1", "Alimentation", "1 896,00 DZD", 30, NeonPurple),
                DonutSlice("2", "Transport", "1 264,00 DZD", 20, NeonBlue),
                DonutSlice("3", "Logement", "948,00 DZD", 15, MintGreen),
                DonutSlice("4", "Loisirs", "948,00 DZD", 15, AmberYellow),
                DonutSlice("5", "Santé", "632,00 DZD", 10, CoralRed),
                DonutSlice("6", "Autres", "632,00 DZD", 10, PinkAccent)
            )
        }

        DonutChart(
            title = stringRes("expenses_by_cat"),
            subtitle = stringRes("this_month"),
            centerTotalText = "${formatter.format(if (totalExpenses > 0) totalExpenses else 6320.0)}\nDZD",
            slices = donutSlices
        )
        
        com.example.ui.components.SpendingLineChartCard(
            transactions = transactions
        )

        // 4. Savings Goal Card
        QuickOverviewHeader(
            title = stringRes("savings_goals"),
            actionText = stringRes("view_all"),
            onActionClick = onGoalsClick
        )

        val dubaiGoal = goals.firstOrNull()
        SavingsGoalCard(
            titleText = dubaiGoal?.title ?: stringRes("default_goal_title"),
            currentAmount = formatter.format(dubaiGoal?.currentAmount ?: 7500.0),
            targetAmount = "${formatter.format(dubaiGoal?.targetAmount ?: 15000.0)} DZD",
            progressPercent = if (dubaiGoal != null && dubaiGoal.targetAmount > 0) ((dubaiGoal.currentAmount / dubaiGoal.targetAmount) * 100).toInt() else 50,
            daysLeftText = stringRes("saving_days").replace("%s", (dubaiGoal?.daysLeft ?: 120).toString()),
            onGoalClick = onGoalsClick
        )

        // 5. Recent Transactions List
        QuickOverviewHeader(
            title = stringRes("recent_transactions"),
            actionText = stringRes("view_all"),
            onActionClick = { onSeeAllClick(1) }
        )

        RecentTransactions(
            transactions = transactions,
            onAddClick = onAddTransactionClick,
            onTransactionClick = {}
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}
