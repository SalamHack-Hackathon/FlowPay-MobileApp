package com.abdallamusa.flowpay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdallamusa.flowpay.data.repository.FakeFlowPayRepositoryImpl
import com.abdallamusa.flowpay.domain.model.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val repository: FakeFlowPayRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiChatUiState())
    val uiState: StateFlow<AiChatUiState> = _uiState.asStateFlow()

    init {
        loadChatHistory()
    }

    private fun loadChatHistory() {
        viewModelScope.launch {
            repository.chatMessages.collect { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    fun sendMessage(message: String) {
        if (message.isBlank()) return

        viewModelScope.launch {
            val userMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = message,
                isFromUser = true,
                timestamp = System.currentTimeMillis()
            )
            repository.addChatMessage(userMessage)
            _uiState.value = _uiState.value.copy(isTyping = true)

            try {
                repository.getAiChatResponse(message)
                _uiState.value = _uiState.value.copy(isTyping = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isTyping = false
                )
            }
        }
    }
}

data class AiChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isTyping: Boolean = false,
    val error: String? = null
)
