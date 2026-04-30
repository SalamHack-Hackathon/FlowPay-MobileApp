package com.abdallamusa.flowpay.domain.model

data class Client(
    val id: String,
    val companyName: String,
    val contactName: String,
    val amount: Double,
    val currency: String = "SAR",
    val status: PaymentStatus,
    val dueDate: String? = null,
    val lastPayment: String? = null,
    val totalTransactions: Double? = null
)

enum class PaymentStatus {
    PENDING,
    PAID
}
