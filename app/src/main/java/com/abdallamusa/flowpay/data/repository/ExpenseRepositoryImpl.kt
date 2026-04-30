package com.abdallamusa.flowpay.data.repository

import com.abdallamusa.flowpay.domain.model.Expense
import com.abdallamusa.flowpay.domain.model.ExpenseCategory
import com.abdallamusa.flowpay.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor() : ExpenseRepository {

    private val dummyExpenses = listOf(
        Expense(
            id = UUID.randomUUID().toString(),
            name = "قهوة ستاربكس",
            amount = 35.0,
            category = ExpenseCategory.FOOD
        ),
        Expense(
            id = UUID.randomUUID().toString(),
            name = "أجرة أوبر",
            amount = 45.0,
            category = ExpenseCategory.TRANSPORT
        ),
        Expense(
            id = UUID.randomUUID().toString(),
            name = "Netflix",
            amount = 55.0,
            category = ExpenseCategory.ENTERTAINMENT
        ),
        Expense(
            id = UUID.randomUUID().toString(),
            name = "فاتورة الكهرباء",
            amount = 350.0,
            category = ExpenseCategory.BILLS
        )
    )

    override fun getExpenses(): Flow<List<Expense>> {
        return flowOf(dummyExpenses)
    }

    override fun getExpenseById(id: String): Flow<Expense?> {
        return flowOf(dummyExpenses.find { it.id == id })
    }

    override suspend fun addExpense(expense: Expense): Result<Expense> {
        val newExpense = expense.copy(id = UUID.randomUUID().toString())
        return Result.success(newExpense)
    }

    override suspend fun updateExpense(expense: Expense): Result<Expense> {
        return Result.success(expense)
    }

    override suspend fun deleteExpense(id: String): Result<Unit> {
        return Result.success(Unit)
    }
}
