package com.abdallamusa.flowpay.domain.repository

import com.abdallamusa.flowpay.domain.model.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun getClients(): Flow<List<Client>>
    fun getClientById(id: String): Flow<Client?>
    suspend fun markAsPaid(clientId: String): Result<Client>
}
