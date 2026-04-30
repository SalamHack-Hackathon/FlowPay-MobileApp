package com.abdallamusa.flowpay.domain.repository

import com.abdallamusa.flowpay.domain.model.Invoice
import kotlinx.coroutines.flow.Flow

interface InvoiceRepository {
    fun getInvoices(): Flow<List<Invoice>>
    fun getInvoiceById(id: String): Flow<Invoice?>
    suspend fun createInvoice(invoice: Invoice): Result<Invoice>
    suspend fun updateInvoice(invoice: Invoice): Result<Invoice>
    suspend fun deleteInvoice(id: String): Result<Unit>
}
