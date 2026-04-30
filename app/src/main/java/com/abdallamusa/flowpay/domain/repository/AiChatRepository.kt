package com.abdallamusa.flowpay.domain.repository

import com.abdallamusa.flowpay.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface AiChatRepository {
    suspend fun sendMessage(message: String): Flow<String>
    fun getChatHistory(): Flow<List<ChatMessage>>
}
