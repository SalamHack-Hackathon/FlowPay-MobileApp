package com.abdallamusa.flowpay.domain.repository

import com.abdallamusa.flowpay.domain.model.ChatMessage
import com.abdallamusa.flowpay.domain.model.Expense
import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FlowPayRepository {
    // State Flows
    val currentUser: Flow<User?>
    val invoices: Flow<List<Invoice>>
    val expenses: Flow<List<Expense>>
    val chatMessages: Flow<List<ChatMessage>>
    
    // Derived Reactive Flows
    val totalIncome: Flow<Double>
    val totalExpenses: Flow<Double>
    val netProfit: Flow<Double>
    val financialScore: Flow<Int>
    
    // Mutation Functions
    suspend fun registerUser(user: User)
    suspend fun addInvoice(invoice: Invoice)
    suspend fun markInvoicePaid(invoiceId: String)
    suspend fun addExpense(expense: Expense)
    suspend fun addChatMessage(message: ChatMessage)
    
    // Mock AI Functions
    suspend fun getAiChatResponse(prompt: String)
    suspend fun generateAiInvoice(prompt: String): Invoice
}
