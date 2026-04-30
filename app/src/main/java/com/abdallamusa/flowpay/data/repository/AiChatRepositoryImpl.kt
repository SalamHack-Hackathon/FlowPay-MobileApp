package com.abdallamusa.flowpay.data.repository

import com.abdallamusa.flowpay.domain.model.ChatMessage
import com.abdallamusa.flowpay.domain.repository.AiChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiChatRepositoryImpl @Inject constructor() : AiChatRepository {

    private val chatHistory = mutableListOf<ChatMessage>()

    override suspend fun sendMessage(message: String): Flow<String> {
        return flow {
            // Simulate streaming response
            val responses = listOf(
                "مرحباً",
                "أنا مساعدك المالي",
                "من FlowPay",
                ".سأساعدك في تحليل",
                "مصروفاتك",
                "وإدارة",
                "مالك بكفاءة"
            )
            
            responses.forEach { chunk ->
                emit(chunk)
                kotlinx.coroutines.delay(100)
            }
        }
    }

    override fun getChatHistory(): Flow<List<ChatMessage>> {
        if (chatHistory.isEmpty()) {
            chatHistory.add(
                ChatMessage(
                    id = UUID.randomUUID().toString(),
                    text = "مرحباً أحمد، أنا مساعدك المالي الذكي من FlowPay. لاحظت وجود بعض التغييرات في نمط إنفاقك هذا الأسبوع. كيف يمكنني مساعدتك اليوم؟",
                    isFromUser = false,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        return flowOf(chatHistory)
    }

    fun addMessage(message: ChatMessage) {
        chatHistory.add(message)
    }
}
