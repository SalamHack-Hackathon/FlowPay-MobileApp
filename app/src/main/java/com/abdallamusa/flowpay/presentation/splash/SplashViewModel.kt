package com.abdallamusa.flowpay.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdallamusa.flowpay.data.local.AppDataStoreManager
import com.abdallamusa.flowpay.domain.repository.FlowPayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStoreManager: AppDataStoreManager,
    private val repository: FlowPayRepository
) : ViewModel() {

    private val _hasRegisteredUser = MutableStateFlow<Boolean?>(null)
    val hasRegisteredUser: StateFlow<Boolean?> = _hasRegisteredUser.asStateFlow()

    fun checkRegisteredUser() {
        viewModelScope.launch {
            val hasUser = dataStoreManager.hasRegisteredUser()
            Log.d("FlowPayDebug", "SplashViewModel checkRegisteredUser: $hasUser")
            _hasRegisteredUser.value = hasUser
            
            // If user exists, restore their data
            if (hasUser) {
                Log.d("FlowPayDebug", "SplashViewModel calling restoreUserData")
                repository.restoreUserData()
                Log.d("FlowPayDebug", "SplashViewModel restoreUserData completed")
            }
        }
    }
}
