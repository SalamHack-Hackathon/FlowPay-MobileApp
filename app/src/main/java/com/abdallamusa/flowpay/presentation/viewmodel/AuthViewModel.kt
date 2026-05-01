package com.abdallamusa.flowpay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdallamusa.flowpay.domain.repository.FlowPayRepository
import com.abdallamusa.flowpay.domain.model.Result
import com.abdallamusa.flowpay.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: FlowPayRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<RegisterUiState>(RegisterUiState())
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    fun registerUser(name: String, email: String, phone: String, password: String, currency: String) {
        viewModelScope.launch {
            _registerState.value = _registerState.value.copy(isLoading = true, error = null)
            try {
                val user = User(
                    id = email, // Using email as ID for simplicity
                    name = name,
                    email = email,
                    phone = phone,
                    currency = currency
                )
                repository.registerUser(user, password, currency)
                repository.restoreUserData()
                _registerState.value = _registerState.value.copy(isLoading = false, isSuccess = true)
            } catch (e: Exception) {
                _registerState.value = _registerState.value.copy(isLoading = false, error = e.message ?: "حدث خطأ أثناء التسجيل")
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(isLoading = true, error = null)
            val result = repository.loginUser(email, password)
            when (result) {
                is Result.Success -> {
                    _loginState.value = _loginState.value.copy(isLoading = false, isSuccess = true, user = result.data)
                }
                is Result.Error -> {
                    _loginState.value = _loginState.value.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun clearLoginState() {
        _loginState.value = LoginUiState()
    }

    fun clearRegisterState() {
        _registerState.value = RegisterUiState()
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val user: User? = null
)

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
