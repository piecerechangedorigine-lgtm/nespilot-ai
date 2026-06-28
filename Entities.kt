package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val isIncome: Boolean,
    val dateMillis: Long,
    val iconName: String
)

@Entity(tableName = "savings_goals")
data class SavingsGoal(
    @PrimaryKey val id: String,
    val title: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val emoji: String,
    val daysLeft: Int
)

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey val id: String,
    val categoryName: String,
    val allocated: Double,
    val spent: Double,
    val colorHex: Long
)

@Entity(tableName = "chat_messages")
data class AiChatMessage(
    @PrimaryKey val id: String,
    val role: String, // "user" or "model"
    val text: String,
    val timestamp: Long
)
