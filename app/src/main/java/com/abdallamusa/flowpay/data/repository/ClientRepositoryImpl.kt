package com.abdallamusa.flowpay.data.repository

import com.abdallamusa.flowpay.domain.model.Client
import com.abdallamusa.flowpay.domain.model.PaymentStatus
import com.abdallamusa.flowpay.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepositoryImpl @Inject constructor() : ClientRepository {

    private val clients = MutableStateFlow(
        listOf(
            Client(
                id = UUID.randomUUID().toString(),
                companyName = "شركة التقنية المتقدمة",
                contactName = "م. طارق عبدالله",
                amount = 45000.0,
                currency = "رس",
                status = PaymentStatus.PENDING,
                dueDate = "15 أكتوبر 2023"
            ),
            Client(
                id = UUID.randomUUID().toString(),
                companyName = "مجموعة الرواد للإعمار",
                contactName = "خالد السالم",
                amount = 120500.0,
                currency = "رس",
                status = PaymentStatus.PAID,
                lastPayment = "10 أكتوبر 2023",
                totalTransactions = 120500.0
            ),
            Client(
                id = UUID.randomUUID().toString(),
                companyName = "شركة الأفق الرقمية",
                contactName = "سارة أحمد",
                amount = 28000.0,
                currency = "رس",
                status = PaymentStatus.PENDING,
                dueDate = "20 أكتوبر 2023"
            ),
            Client(
                id = UUID.randomUUID().toString(),
                companyName = "مؤسسة النخبة للخدمات",
                contactName = "عمر الفهد",
                amount = 65000.0,
                currency = "رس",
                status = PaymentStatus.PAID,
                lastPayment = "05 أكتوبر 2023",
                totalTransactions = 65000.0
            ),
            Client(
                id = UUID.randomUUID().toString(),
                companyName = "شركة التميز التجاري",
                contactName = "نورة العتيبي",
                amount = 32000.0,
                currency = "رس",
                status = PaymentStatus.PENDING,
                dueDate = "25 أكتوبر 2023"
            )
        )
    )

    override fun getClients(): Flow<List<Client>> {
        return clients
    }

    override fun getClientById(id: String): Flow<Client?> {
        return flowOf(clients.value.find { it.id == id })
    }

    override suspend fun markAsPaid(clientId: String): Result<Client> {
        clients.update { currentClients ->
            currentClients.map { client ->
                if (client.id == clientId) {
                    client.copy(
                        status = PaymentStatus.PAID,
                        lastPayment = "اليوم",
                        totalTransactions = client.amount
                    )
                } else {
                    client
                }
            }
        }
        val updatedClient = clients.value.find { it.id == clientId }
        return if (updatedClient != null) {
            Result.success(updatedClient)
        } else {
            Result.failure(Exception("Client not found"))
        }
    }
}
