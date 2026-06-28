package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AiChatMessage
import com.example.data.model.Budget
import com.example.data.model.SavingsGoal
import com.example.data.model.Transaction
import com.example.data.repository.FinanceRepository
import com.example.network.GeminiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinanceViewModel(private val repository: FinanceRepository) : ViewModel() {

    val transactions: StateFlow<List<Transaction>> = repository.transactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val goals: StateFlow<List<SavingsGoal>> = repository.goals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val budgets: StateFlow<List<Budget>> = repository.budgets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<AiChatMessage>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedTab = MutableStateFlow(0) // 0: Accueil, 1: Transactions, 2: AI Copilot, 3: Budgets, 4: Plus
    val selectedTab = _selectedTab.asStateFlow()

    private val _isPro = MutableStateFlow(false) // Free vs Pro subscription
    val isPro = _isPro.asStateFlow()

    private val _language = MutableStateFlow("FR") // FR, EN, AR
    val language = _language.asStateFlow()

    private val _dailyAdviceVisible = MutableStateFlow(false)
    val dailyAdviceVisible = _dailyAdviceVisible.asStateFlow()

    private val _dailyAdviceText = MutableStateFlow("Conseil IA Gemini en cours d'analyse...")
    val dailyAdviceText = _dailyAdviceText.asStateFlow()

    private val _isLoadingAdvice = MutableStateFlow(false)
    val isLoadingAdvice = _isLoadingAdvice.asStateFlow()

    private val _addTxVisible = MutableStateFlow(false)
    val addTxVisible = _addTxVisible.asStateFlow()

    private val _isSendingChat = MutableStateFlow(false)
    val isSendingChat = _isSendingChat.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun togglePro() {
        _isPro.value = !_isPro.value
    }

    fun setLanguage(lang: String) {
        _language.value = lang
    }

    fun showDailyAdvice() {
        _dailyAdviceVisible.value = true
        _isLoadingAdvice.value = true
        viewModelScope.launch {
            val prompt = "Donne un conseil financier stratégique du jour pour Yacine en une phrase percutante en français basé sur un solde de 6 220 DZD et des charges maîtrisées."
            val advice = GeminiService.getFinancialAdvice(prompt)
            _dailyAdviceText.value = advice
            _isLoadingAdvice.value = false
        }
    }

    fun dismissDailyAdvice() {
        _dailyAdviceVisible.value = false
    }

    fun openAddTransaction() {
        _addTxVisible.value = true
    }

    fun closeAddTransaction() {
        _addTxVisible.value = false
    }

    fun submitNewTransaction(title: String, category: String, amount: Double, isIncome: Boolean) {
        viewModelScope.launch {
            repository.addTransaction(title, category, amount, isIncome)
            closeAddTransaction()
        }
    }

    fun addGoal(title: String, targetAmount: Double, emoji: String, daysLeft: Int) {
        viewModelScope.launch {
            repository.addGoal(title, targetAmount, emoji, daysLeft)
        }
    }

    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            repository.deleteGoal(goalId)
        }
    }

    fun depositToGoal(amount: Double) {
        viewModelScope.launch {
            repository.addGoalDeposit(amount)
        }
    }

    fun sendChatMessage(userText: String) {
        if (userText.isBlank() || _isSendingChat.value) return
        _isSendingChat.value = true
        viewModelScope.launch {
            repository.sendUserChatMessage(userText) { prompt ->
                GeminiService.getFinancialAdvice(prompt)
            }
            _isSendingChat.value = false
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
        }
    }

    class Factory(private val repository: FinanceRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FinanceViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
