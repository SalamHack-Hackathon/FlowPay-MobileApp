package com.abdallamusa.flowpay.domain.model

enum class InvoiceStatus {
    DRAFT,
    PENDING,
    PAID,
    CANCELLED
}

data class Invoice(
    val id: String,
    val clientName: String,
    val amount: Double,
    val service: String,
    val status: InvoiceStatus = InvoiceStatus.DRAFT,
    val date: Long = System.currentTimeMillis()
)
