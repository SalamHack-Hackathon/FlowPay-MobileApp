package com.abdallamusa.flowpay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdallamusa.flowpay.domain.repository.FlowPayRepository
import com.abdallamusa.flowpay.domain.model.Invoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class CreateInvoiceUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CreateInvoiceViewModel @Inject constructor(
    private val repository: FlowPayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateInvoiceUiState())
    val uiState: StateFlow<CreateInvoiceUiState> = _uiState.asStateFlow()

    init {
        println("DEBUG: CreateInvoiceViewModel initialized - repository instance: ${repository.hashCode()}")
    }

    fun createInvoice(clientName: String, amount: Double, service: String) {
        viewModelScope.launch {
            println("DEBUG: CreateInvoiceViewModel creating invoice for $clientName")
            _uiState.value = CreateInvoiceUiState(isLoading = true)
            
            val invoice = Invoice(
                id = UUID.randomUUID().toString(),
                clientName = clientName,
                amount = amount,
                service = service,
                date = System.currentTimeMillis()
            )
            
            try {
                repository.addInvoice(invoice)
                println("DEBUG: CreateInvoiceViewModel invoice added successfully")
                _uiState.value = CreateInvoiceUiState(isSuccess = true)
            } catch (e: Exception) {
                println("DEBUG: CreateInvoiceViewModel error: ${e.message}")
                _uiState.value = CreateInvoiceUiState(
                    error = e.message ?: "Failed to create invoice"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = CreateInvoiceUiState()
    }
}
