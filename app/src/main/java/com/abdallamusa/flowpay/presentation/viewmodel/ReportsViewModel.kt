package com.abdallamusa.flowpay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdallamusa.flowpay.domain.repository.FlowPayRepository
import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.InvoiceStatus
import com.abdallamusa.flowpay.utils.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: FlowPayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()
    val currency: Flow<String> = repository.userCurrency

    init {
        Log.d("FlowPayDebug", "ReportsViewModel initialized - repository instance: ${repository.hashCode()}")
        loadReportData()
    }

    private fun loadReportData() {
        viewModelScope.launch {
            Log.d("FlowPayDebug", "ReportsViewModel starting to collect report data")
            repository.totalIncome.combine(repository.totalExpenses) { income, expenses ->
                Pair(income, expenses)
            }.combine(repository.netProfit) { (income, expenses), profit ->
                Triple(income, expenses, profit)
            }.combine(repository.financialScore) { (income, expenses, profit), score ->
                Quadruple(income, expenses, profit, score)
            }.combine(repository.invoices) { quad, invoices ->
                Log.d("FlowPayDebug", "ReportsViewModel received invoices update - count: ${invoices.size}")
                Pair(quad, invoices)
            }.combine(repository.expenses) { (quad, invoices), expenses ->
                val income = quad.first
                val expensesVal = quad.second
                val profit = quad.third
                val score = quad.fourth
                val invoiceList = invoices
                val expenseList = expenses

                // Calculate profit change percentage (simplified - compares to zero for new users)
                val profitChange = if (profit > 0) {
                    val previousProfit = 0.0 // For new users, previous is 0
                    val change = ((profit - previousProfit) / previousProfit) * 100
                    if (change >= 0) "+${String.format("%.1f", change)}%" else "${String.format("%.1f", change)}%"
                } else "0%"

                // Build chart data from actual invoices and expenses
                val (incomeData, expenseData, months) = buildChartData(invoiceList, expenseList)

                // Build top categories from actual invoice data
                val topCategories = buildTopCategories(invoiceList)
                
                // Get pending invoices for dashboard display
                val pendingInvoices = invoiceList.filter { it.status == InvoiceStatus.PENDING }
                Log.d("FlowPayDebug", "ReportsViewModel pending invoices: ${pendingInvoices.size}")

                ReportsUiState(
                    totalNetProfit = profit,
                    profitChange = profitChange,
                    financialRating = score,
                    incomeData = incomeData,
                    expenseData = expenseData,
                    months = months,
                    topCategories = topCategories,
                    pendingInvoices = pendingInvoices
                )
            }.collect { state ->
                Log.d("FlowPayDebug", "ReportsViewModel updated uiState - profit: ${state.totalNetProfit}, pendingInvoices: ${state.pendingInvoices.size}")
                _uiState.value = state
            }
        }
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

    private fun buildChartData(
        invoices: List<com.abdallamusa.flowpay.domain.model.Invoice>,
        expenses: List<com.abdallamusa.flowpay.domain.model.Expense>
    ): Triple<List<Float>, List<Float>, List<String>> {
        // Group by month and calculate totals
        val monthlyIncome = mutableMapOf<String, Float>()
        val monthlyExpenses = mutableMapOf<String, Float>()

        // Process invoices (income)
        invoices.filter { it.status == com.abdallamusa.flowpay.domain.model.InvoiceStatus.PAID }
            .forEach { invoice ->
                val month = getMonthName(invoice.date)
                monthlyIncome[month] = monthlyIncome.getOrDefault(month, 0f) + invoice.amount.toFloat()
            }

        // Process expenses
        expenses.forEach { expense ->
            val month = getMonthName(expense.date)
            monthlyExpenses[month] = monthlyExpenses.getOrDefault(month, 0f) + expense.amount.toFloat()
        }

        // Get last 6 months
        val allMonths = listOf(
            Strings.ArabicMonths.MAY, Strings.ArabicMonths.JUNE, Strings.ArabicMonths.JULY,
            Strings.ArabicMonths.AUGUST, Strings.ArabicMonths.SEPTEMBER, Strings.ArabicMonths.OCTOBER
        )

        val incomeList = allMonths.map { monthlyIncome.getOrDefault(it, 0f) }
        val expenseList = allMonths.map { monthlyExpenses.getOrDefault(it, 0f) }

        return Triple(incomeList, expenseList, allMonths)
    }

    private fun getMonthName(timestamp: Long): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return when (calendar.get(java.util.Calendar.MONTH)) {
            java.util.Calendar.MAY -> Strings.ArabicMonths.MAY
            java.util.Calendar.JUNE -> Strings.ArabicMonths.JUNE
            java.util.Calendar.JULY -> Strings.ArabicMonths.JULY
            java.util.Calendar.AUGUST -> Strings.ArabicMonths.AUGUST
            java.util.Calendar.SEPTEMBER -> Strings.ArabicMonths.SEPTEMBER
            java.util.Calendar.OCTOBER -> Strings.ArabicMonths.OCTOBER
            else -> Strings.ArabicMonths.OCTOBER
        }
    }

    private fun buildTopCategories(
        invoices: List<com.abdallamusa.flowpay.domain.model.Invoice>
    ): List<ServiceCategory> {
        if (invoices.isEmpty()) return emptyList()

        // Group by service type and sum amounts
        val categoryTotals = invoices
            .filter { it.status == com.abdallamusa.flowpay.domain.model.InvoiceStatus.PAID }
            .groupBy { it.service }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
            .take(3)

        if (categoryTotals.isEmpty()) return emptyList()

        val maxAmount = categoryTotals.maxOf { it.second }

        return categoryTotals.map { (name, amount) ->
            ServiceCategory(
                name = name,
                amount = amount,
                progress = if (maxAmount > 0) (amount / maxAmount).toFloat() else 0f
            )
        }
    }
}

data class ReportsUiState(
    val totalNetProfit: Double = 0.0,
    val profitChange: String = "",
    val financialRating: Int = 0,
    val incomeData: List<Float> = emptyList(),
    val expenseData: List<Float> = emptyList(),
    val months: List<String> = emptyList(),
    val topCategories: List<ServiceCategory> = emptyList(),
    val pendingInvoices: List<Invoice> = emptyList()
)

data class ServiceCategory(
    val name: String,
    val amount: Double,
    val progress: Float
)
