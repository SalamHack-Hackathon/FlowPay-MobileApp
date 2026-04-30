package com.abdallamusa.flowpay.data.repository

import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.InvoiceStatus
import com.abdallamusa.flowpay.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvoiceRepositoryImpl @Inject constructor() : InvoiceRepository {

    private val dummyInvoices = listOf(
        Invoice(
            id = UUID.randomUUID().toString(),
            clientName = "أحمد محمد",
            amount = 5000.0,
            service = "تصميم الشعار",
            status = InvoiceStatus.PENDING
        ),
        Invoice(
            id = UUID.randomUUID().toString(),
            clientName = "شركة التقنية",
            amount = 15000.0,
            service = "تطوير تطبيق الجوال",
            status = InvoiceStatus.PAID
        ),
        Invoice(
            id = UUID.randomUUID().toString(),
            clientName = "مؤسسة الأفق",
            amount = 8500.0,
            service = "استشارات تسويقية",
            status = InvoiceStatus.DRAFT
        )
    )

    override fun getInvoices(): Flow<List<Invoice>> {
        return flowOf(dummyInvoices)
    }

    override fun getInvoiceById(id: String): Flow<Invoice?> {
        return flowOf(dummyInvoices.find { it.id == id })
    }

    override suspend fun createInvoice(invoice: Invoice): Result<Invoice> {
        val newInvoice = invoice.copy(id = UUID.randomUUID().toString())
        return Result.success(newInvoice)
    }

    override suspend fun updateInvoice(invoice: Invoice): Result<Invoice> {
        return Result.success(invoice)
    }

    override suspend fun deleteInvoice(id: String): Result<Unit> {
        return Result.success(Unit)
    }
}
