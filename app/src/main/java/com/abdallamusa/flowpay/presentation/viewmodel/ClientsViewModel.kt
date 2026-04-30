package com.abdallamusa.flowpay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdallamusa.flowpay.data.repository.FakeFlowPayRepositoryImpl
import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.InvoiceStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ClientFilter {
    ALL, PAID, PENDING
}

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repository: FakeFlowPayRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientsUiState())
    val uiState: StateFlow<ClientsUiState> = _uiState.asStateFlow()

    init {
        loadInvoices()
    }

    private fun loadInvoices() {
        viewModelScope.launch {
            repository.invoices.collect { invoices ->
                val paid = invoices.count { it.status == InvoiceStatus.PAID }
                val pending = invoices.count { it.status == InvoiceStatus.PENDING }
                val filtered = filterInvoices(invoices, _uiState.value.selectedFilter)
                _uiState.value = _uiState.value.copy(
                    allInvoices = invoices,
                    filteredInvoices = filtered,
                    paidCount = paid,
                    pendingCount = pending
                )
            }
        }
    }

    private fun filterInvoices(invoices: List<Invoice>, filter: ClientFilter): List<Invoice> {
        return when (filter) {
            ClientFilter.ALL -> invoices
            ClientFilter.PAID -> invoices.filter { it.status == InvoiceStatus.PAID }
            ClientFilter.PENDING -> invoices.filter { it.status == InvoiceStatus.PENDING }
        }
    }

    fun setFilter(filter: ClientFilter) {
        val currentAll = _uiState.value.allInvoices
        _uiState.value = _uiState.value.copy(
            selectedFilter = filter,
            filteredInvoices = filterInvoices(currentAll, filter)
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
    val filteredInvoices: List<Invoice> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val paidCount: Int = 0,
    val pendingCount: Int = 0,
    val selectedFilter: ClientFilter = ClientFilter.PENDING
)
