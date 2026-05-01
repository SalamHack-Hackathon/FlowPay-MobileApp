package com.abdallamusa.flowpay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdallamusa.flowpay.domain.repository.FlowPayRepository
import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.InvoiceStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientSummary(
    val clientName: String,
    val totalDue: Double,
    val paidAmount: Double,
    val pendingAmount: Double,
    val invoiceCount: Int,
    val paidCount: Int,
    val pendingCount: Int,
    val pendingInvoiceIds: List<String> = emptyList()
)

enum class ClientFilter {
    ALL, PAID, PENDING
}

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repository: FlowPayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientsUiState())
    val uiState: StateFlow<ClientsUiState> = _uiState.asStateFlow()
    val currency: Flow<String> = repository.userCurrency

    init {
        Log.d("FlowPayDebug", "ClientsViewModel initialized - repository instance: ${repository.hashCode()}")
        loadInvoices()
    }

    private fun loadInvoices() {
        viewModelScope.launch {
            Log.d("FlowPayDebug", "ClientsViewModel starting to collect invoices")
            repository.invoices.collect { invoices ->
                Log.d("FlowPayDebug", "ClientsViewModel received invoices update - count: ${invoices.size}")
                val clientSummaries = groupInvoicesByClient(invoices)
                val filtered = filterClients(clientSummaries, _uiState.value.selectedFilter)
                _uiState.value = _uiState.value.copy(
                    allInvoices = invoices,
                    clientSummaries = clientSummaries,
                    filteredClients = filtered
                )
                Log.d("FlowPayDebug", "ClientsViewModel updated uiState - filtered clients: ${filtered.size}")
            }
        }
    }

    private fun groupInvoicesByClient(invoices: List<Invoice>): List<ClientSummary> {
        return invoices
            .groupBy { it.clientName }
            .map { (clientName, clientInvoices) ->
                val paidInvoices = clientInvoices.filter { it.status == InvoiceStatus.PAID }
                val pendingInvoices = clientInvoices.filter { it.status == InvoiceStatus.PENDING }
                val paidAmount = paidInvoices.sumOf { it.amount }
                val pendingAmount = pendingInvoices.sumOf { it.amount }
                
                ClientSummary(
                    clientName = clientName,
                    totalDue = paidAmount + pendingAmount,
                    paidAmount = paidAmount,
                    pendingAmount = pendingAmount,
                    invoiceCount = clientInvoices.size,
                    paidCount = paidInvoices.size,
                    pendingCount = pendingInvoices.size,
                    pendingInvoiceIds = pendingInvoices.map { it.id }
                )
            }
            .sortedByDescending { it.totalDue }
    }

    private fun filterClients(clients: List<ClientSummary>, filter: ClientFilter): List<ClientSummary> {
        return when (filter) {
            ClientFilter.ALL -> clients
            ClientFilter.PAID -> clients.filter { it.pendingCount == 0 && it.paidCount > 0 }
            ClientFilter.PENDING -> clients.filter { it.pendingCount > 0 }
        }
    }

    fun setFilter(filter: ClientFilter) {
        val currentAll = _uiState.value.clientSummaries
        _uiState.value = _uiState.value.copy(
            selectedFilter = filter,
            filteredClients = filterClients(currentAll, filter)
        )
    }

    fun markAsPaid(invoiceId: String) {
        viewModelScope.launch {
            repository.markInvoicePaid(invoiceId)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class ClientsUiState(
    val allInvoices: List<Invoice> = emptyList(),
    val clientSummaries: List<ClientSummary> = emptyList(),
    val filteredClients: List<ClientSummary> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedFilter: ClientFilter = ClientFilter.PENDING
)
