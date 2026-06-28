package com.example.data.repository

import com.example.data.local.FinanceDao
import com.example.data.model.AiChatMessage
import com.example.data.model.Budget
import com.example.data.model.SavingsGoal
import com.example.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID

class FinanceRepository(private val dao: FinanceDao) {
    val transactions: Flow<List<Transaction>> = dao.getAllTransactions()
    val goals: Flow<List<SavingsGoal>> = dao.getAllGoals()
    val budgets: Flow<List<Budget>> = dao.getAllBudgets()
    val chatMessages: Flow<List<AiChatMessage>> = dao.getAllChatMessages()

    suspend fun seedInitialDataIfEmpty() {
        val currentTxs = transactions.first()
        if (currentTxs.isEmpty()) {
            val now = System.currentTimeMillis()
            val day = 86400000L

            dao.insertTransaction(Transaction(UUID.randomUUID().toString(), "Carrefour Market", "Alimentation", 850.0, false, now, "ShoppingCart"))
            dao.insertTransaction(Transaction(UUID.randomUUID().toString(), "Abonnement Apple Music", "Loisirs", 899.0, false, now - day, "MusicNote"))
            dao.insertTransaction(Transaction(UUID.randomUUID().toString(), "Course Uber", "Transport", 450.0, false, now - day * 2, "DirectionsCar"))
            dao.insertTransaction(Transaction(UUID.randomUUID().toString(), "Pharmacie Centrale", "Santé", 632.0, false, now - day * 4, "LocalHospital"))
            dao.insertTransaction(Transaction(UUID.randomUUID().toString(), "Airbnb Location Dubaï", "Logement", 948.0, false, now - day * 8, "Home"))
            dao.insertTransaction(Transaction(UUID.randomUUID().toString(), "Salaire TechCorp", "Revenus", 12540.0, true, now - day * 12, "AccountBalanceWallet"))
            dao.insertTransaction(Transaction(UUID.randomUUID().toString(), "Café & Brunch", "Autres", 300.0, false, now - day * 15, "LocalCafe"))
        }

        val currentGoals = goals.first()
        if (currentGoals.isEmpty()) {
            dao.insertGoal(SavingsGoal("dubai_trip", "Voyage à Dubaï 🌴", 15000.0, 7500.0, "🌴", 120))
        }

        val currentBudgets = budgets.first()
        if (currentBudgets.isEmpty()) {
            dao.insertBudget(Budget("b1", "Alimentation", 1896.0, 850.0, 0xFF8A2BE2))
            dao.insertBudget(Budget("b2", "Transport", 1264.0, 450.0, 0xFF3B82F6))
            dao.insertBudget(Budget("b3", "Logement", 948.0, 948.0, 0xFF22C55E))
            dao.insertBudget(Budget("b4", "Loisirs", 948.0, 899.0, 0xFFF59E0B))
            dao.insertBudget(Budget("b5", "Santé", 632.0, 632.0, 0xFFEF4444))
            dao.insertBudget(Budget("b6", "Autres", 632.0, 300.0, 0xFFEC4899))
        }

        val currentChat = chatMessages.first()
        if (currentChat.isEmpty()) {
            dao.insertChatMessage(AiChatMessage(
                id = UUID.randomUUID().toString(),
                role = "model",
                text = "Bonjour Yacine ! 👋 Je suis votre Copilot financier NESPILOT AI propulsé par Gemini 2.5. J'ai analysé vos comptes : vous avez dépensé 18% de moins que le mois dernier. Continuez ainsi ! Comment puis-je vous assister aujourd'hui ?",
                timestamp = System.currentTimeMillis()
            ))
        }
    }

    suspend fun addTransaction(title: String, category: String, amount: Double, isIncome: Boolean) {
        dao.insertTransaction(Transaction(
            id = UUID.randomUUID().toString(),
            title = title,
            category = category,
            amount = amount,
            isIncome = isIncome,
            dateMillis = System.currentTimeMillis(),
            iconName = if (isIncome) "AccountBalanceWallet" else "ShoppingCart"
        ))

        // Update savings goal if income or dubai
        if (category.contains("Dubaï", true) || category.contains("Épargne", true)) {
            val goalList = goals.first()
            goalList.firstOrNull()?.let { g ->
                dao.insertGoal(g.copy(currentAmount = g.currentAmount + amount))
            }
        }
    }

    suspend fun addGoalDeposit(amount: Double) {
        val goalList = goals.first()
        goalList.firstOrNull()?.let { g ->
            val newAmount = (g.currentAmount + amount).coerceAtMost(g.targetAmount)
            dao.insertGoal(g.copy(currentAmount = newAmount))
        }
    }

    suspend fun addGoal(title: String, targetAmount: Double, emoji: String, daysLeft: Int) {
        dao.insertGoal(SavingsGoal(
            id = UUID.randomUUID().toString(),
            title = title,
            targetAmount = targetAmount,
            currentAmount = 0.0,
            emoji = emoji,
            daysLeft = daysLeft
        ))
    }

    suspend fun deleteGoal(goalId: String) {
        dao.deleteGoal(goalId)
    }

    suspend fun sendUserChatMessage(prompt: String, getAiReply: suspend (String) -> String) {
        val userMsg = AiChatMessage(UUID.randomUUID().toString(), "user", prompt, System.currentTimeMillis())
        dao.insertChatMessage(userMsg)

        val replyText = getAiReply(prompt)
        val aiMsg = AiChatMessage(UUID.randomUUID().toString(), "model", replyText, System.currentTimeMillis() + 100)
        dao.insertChatMessage(aiMsg)
    }

    suspend fun clearChatHistory() {
        dao.clearChat()
        dao.insertChatMessage(AiChatMessage(
            id = UUID.randomUUID().toString(),
            role = "model",
            text = "Conversation réinitialisée. Posez-moi n'importe quelle question sur vos finances ou votre budget !",
            timestamp = System.currentTimeMillis()
        ))
    }
}
