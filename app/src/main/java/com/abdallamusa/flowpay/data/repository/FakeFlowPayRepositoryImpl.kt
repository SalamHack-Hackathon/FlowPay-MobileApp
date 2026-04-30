package com.abdallamusa.flowpay.data.repository

import com.abdallamusa.flowpay.domain.model.ChatMessage
import com.abdallamusa.flowpay.domain.model.Expense
import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.InvoiceStatus
import com.abdallamusa.flowpay.domain.model.User
import com.abdallamusa.flowpay.domain.repository.FlowPayRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeFlowPayRepositoryImpl @Inject constructor() : FlowPayRepository {

    // MutableStateFlows for state management
    private val _currentUser = MutableStateFlow<User?>(null)
    private val _invoices = MutableStateFlow<List<Invoice>>(emptyList())
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())

    // Public read-only StateFlows
    override val currentUser: Flow<User?> = _currentUser.asStateFlow()
    override val invoices: Flow<List<Invoice>> = _invoices.asStateFlow()
    override val expenses: Flow<List<Expense>> = _expenses.asStateFlow()
    override val chatMessages: Flow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Derived Reactive Flows
    override val totalIncome: Flow<Double> = _invoices.map { invoices ->
        invoices.filter { it.status == InvoiceStatus.PAID }.sumOf { it.amount }
    }

    override val totalExpenses: Flow<Double> = _expenses.map { expenses ->
        expenses.sumOf { it.amount }
    }

    override val netProfit: Flow<Double> = combine(totalIncome, totalExpenses) { income, expenses ->
        income - expenses
    }

    override val financialScore: Flow<Int> = combine(totalIncome, totalExpenses) { income, expenses ->
        val total = income + expenses
        if (total == 0.0) {
            0
        } else {
            ((income / total) * 100).toInt()
        }
    }

    // Mutation Functions
    override suspend fun registerUser(user: User) {
        _currentUser.value = user
    }

    override suspend fun addInvoice(invoice: Invoice) {
        _invoices.value += invoice
    }

    override suspend fun markInvoicePaid(invoiceId: String) {
        _invoices.value = _invoices.value.map { invoice ->
            if (invoice.id == invoiceId) {
                invoice.copy(status = InvoiceStatus.PAID)
            } else {
                invoice
            }
        }
    }

    override suspend fun addExpense(expense: Expense) {
        _expenses.value = _expenses.value + expense
    }

    override suspend fun addChatMessage(message: ChatMessage) {
        _chatMessages.value = _chatMessages.value + message
    }

    // Mock AI Functions
    override suspend fun getAiChatResponse(prompt: String) {
        delay(1500) // Simulate AI thinking time
        val aiResponse = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = "بناءً على بياناتك، أداؤك المالي ممتاز. أنصحك بمتابعة تقليل المصروفات.",
            isFromUser = false,
            timestamp = System.currentTimeMillis()
        )
        addChatMessage(aiResponse)
    }

    override suspend fun generateAiInvoice(prompt: String): Invoice {
        delay(1500) // Simulate AI thinking time
        
        // Extract amount (first sequence of digits)
        val amount = Regex("\\d+").find(prompt)?.value?.toDoubleOrNull() ?: 0.0
        
        // Extract client name (first word)
        val clientName = prompt.split("\\s+".toRegex()).firstOrNull() ?: "Unknown Client"
        
        return Invoice(
            id = UUID.randomUUID().toString(),
            clientName = clientName,
            amount = amount,
            service = "AI Generated",
            date = System.currentTimeMillis(),
            status = InvoiceStatus.DRAFT
        )
    }
}
