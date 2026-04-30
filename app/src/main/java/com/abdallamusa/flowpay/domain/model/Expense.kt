package com.abdallamusa.flowpay.domain.model

data class Expense(
    val id: String,
    val name: String,
    val amount: Double,
    val category: ExpenseCategory,
    val date: Long = System.currentTimeMillis()
)
