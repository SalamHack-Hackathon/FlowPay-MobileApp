package com.abdallamusa.flowpay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdallamusa.flowpay.data.repository.FakeFlowPayRepositoryImpl
import com.abdallamusa.flowpay.domain.model.Invoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AiInvoiceUiState(
    val isLoading: Boolean = false,
    val generatedInvoice: Invoice? = null,
    val error: String? = null
)

@HiltViewModel
class AiSmartInvoiceViewModel @Inject constructor(
    private val repository: FakeFlowPayRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiInvoiceUiState())
    val uiState: StateFlow<AiInvoiceUiState> = _uiState.asStateFlow()

    fun generateInvoice(prompt: String) {
        viewModelScope.launch {
            _uiState.value = AiInvoiceUiState(isLoading = true)
            
            try {
                val invoice = repository.generateAiInvoice(prompt)
                _uiState.value = AiInvoiceUiState(generatedInvoice = invoice)
            } catch (e: Exception) {
                _uiState.value = AiInvoiceUiState(
                    error = e.message ?: "Failed to generate invoice"
                )
            }
        }
    }

    fun confirmInvoice() {
        viewModelScope.launch {
            _uiState.value.generatedInvoice?.let { invoice ->
                try {
                    repository.addInvoice(invoice)
                    _uiState.value = AiInvoiceUiState()
                } catch (e: Exception) {
                    _uiState.value = AiInvoiceUiState(
                        error = e.message ?: "Failed to add invoice"
                    )
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = AiInvoiceUiState()
    }

    fun clearInvoice() {
        _uiState.value = _uiState.value.copy(generatedInvoice = null)
    }
}
