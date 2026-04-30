package com.abdallamusa.flowpay.domain.repository

import com.abdallamusa.flowpay.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpenses(): Flow<List<Expense>>
    fun getExpenseById(id: String): Flow<Expense?>
    suspend fun addExpense(expense: Expense): Result<Expense>
    suspend fun updateExpense(expense: Expense): Result<Expense>
    suspend fun deleteExpense(id: String): Result<Unit>
}
