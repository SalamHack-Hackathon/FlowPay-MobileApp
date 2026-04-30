package com.abdallamusa.flowpay.domain.model

import com.abdallamusa.flowpay.utils.Strings

data class Transaction(
    val id: String,
    val companyName: String,
    val invoiceNumber: String,
    val amount: Double,
    val currency: String = Strings.Common.CURRENCY,
    val status: TransactionStatus,
    val description: String? = null,
    val date: Long = System.currentTimeMillis()
)

enum class TransactionStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    FAILED
}
