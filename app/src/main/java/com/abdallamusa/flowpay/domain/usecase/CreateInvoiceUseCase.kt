package com.abdallamusa.flowpay.domain.usecase

import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.repository.InvoiceRepository
import javax.inject.Inject

class CreateInvoiceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {
    suspend operator fun invoke(invoice: Invoice): Result<Invoice> {
        return invoiceRepository.createInvoice(invoice)
    }
}
