package com.abdallamusa.flowpay.domain.usecase

import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.InvoiceStatus
import java.util.UUID
import javax.inject.Inject

class GenerateAiInvoiceUseCase @Inject constructor() {
    
    // TODO: Integrate with Gemini Flash API in JSON mode
    // For now, this is a dummy implementation that parses simple text
    suspend operator fun invoke(prompt: String): Result<Invoice> {
        return try {
            // Dummy parsing logic - will be replaced with Gemini API
            val invoice = parseInvoiceFromPrompt(prompt)
            Result.success(invoice)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun parseInvoiceFromPrompt(prompt: String): Invoice {
        // Simple regex-based parsing for demo purposes
        // Expected format: "فاتورة لـ [name] بـ [amount] مقابل [service]"
        val clientName = extractClientName(prompt) ?: "عميل غير معروف"
        val amount = extractAmount(prompt) ?: 0.0
        val service = extractService(prompt) ?: "خدمة عامة"
        
        return Invoice(
            id = UUID.randomUUID().toString(),
            clientName = clientName,
            amount = amount,
            service = service,
            status = InvoiceStatus.DRAFT
        )
    }
    
    private fun extractClientName(prompt: String): String? {
        // Extract name after "لـ" or "ل"
        val regex = Regex("(?:لـ|ل)\\s*([\\u0600-\\u06FF\\s]+)\\s*بـ")
        val match = regex.find(prompt)
        return match?.groupValues?.get(1)?.trim()
    }
    
    private fun extractAmount(prompt: String): Double? {
        // Extract number after "بـ" or "ب"
        val regex = Regex("(?:بـ|ب)\\s*(\\d+(?:\\.\\d+)?)")
        val match = regex.find(prompt)
        return match?.groupValues?.get(1)?.toDoubleOrNull()
    }
    
    private fun extractService(prompt: String): String? {
        // Extract text after "مقابل" or "لـ"
        val regex = Regex("(?:مقابل|لـ)\\s*([\\u0600-\\u06FF\\s]+)$")
        val match = regex.find(prompt)
        return match?.groupValues?.get(1)?.trim()
    }
}
