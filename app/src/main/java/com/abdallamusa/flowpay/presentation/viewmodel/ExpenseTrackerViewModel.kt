package com.abdallamusa.flowpay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdallamusa.flowpay.domain.repository.FlowPayRepository
import com.abdallamusa.flowpay.domain.model.Expense
import com.abdallamusa.flowpay.domain.model.ExpenseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ExpenseTrackerUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ExpenseTrackerViewModel @Inject constructor(
    private val repository: FlowPayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseTrackerUiState())
    val uiState: StateFlow<ExpenseTrackerUiState> = _uiState.asStateFlow()

    fun addExpense(name: String, amount: Double, category: ExpenseCategory) {
        viewModelScope.launch {
            _uiState.value = ExpenseTrackerUiState(isLoading = true)
            
            val expense = Expense(
                id = UUID.randomUUID().toString(),
                name = name,
                amount = amount,
                category = category,
                date = System.currentTimeMillis()
            )
            
            try {
                repository.addExpense(expense)
                _uiState.value = ExpenseTrackerUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = ExpenseTrackerUiState(
                    error = e.message ?: "Failed to add expense"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = ExpenseTrackerUiState()
    }
}
