package com.abdallamusa.flowpay.domain.usecase

import com.abdallamusa.flowpay.domain.model.Expense
import com.abdallamusa.flowpay.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense): Result<Expense> {
        return expenseRepository.addExpense(expense)
    }
}
