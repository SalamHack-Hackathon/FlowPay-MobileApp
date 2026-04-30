package com.abdallamusa.flowpay.data.repository

import com.abdallamusa.flowpay.utils.Strings
import com.abdallamusa.flowpay.domain.model.Transaction
import com.abdallamusa.flowpay.domain.model.TransactionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DashboardRepository {

    fun getTransactions(): Flow<List<Transaction>> {
        val dummyTransactions = listOf(
            Transaction(
                id = "1",
                companyName = Strings.SampleData.COMPANY_1,
                invoiceNumber = "INV-2023-089",
                amount = 12500.0,
                status = TransactionStatus.PENDING,
                description = Strings.SampleData.DESCRIPTION_1
            ),
            Transaction(
                id = "2",
                companyName = Strings.SampleData.COMPANY_2,
                invoiceNumber = "INV-2023-092",
                amount = 4200.0,
                status = TransactionStatus.CONFIRMED,
                description = Strings.SampleData.DESCRIPTION_2
            ),
            Transaction(
                id = "3",
                companyName = Strings.SampleData.COMPANY_3,
                invoiceNumber = "INV-2023-095",
                amount = 8900.0,
                status = TransactionStatus.PENDING,
                description = Strings.SampleData.DESCRIPTION_3
            ),
            Transaction(
                id = "4",
                companyName = Strings.SampleData.COMPANY_4,
                invoiceNumber = "INV-2023-098",
                amount = 15600.0,
                status = TransactionStatus.COMPLETED,
                description = Strings.SampleData.DESCRIPTION_4
            ),
            Transaction(
                id = "5",
                companyName = Strings.SampleData.COMPANY_5,
                invoiceNumber = "INV-2023-101",
                amount = 7500.0,
                status = TransactionStatus.PENDING,
                description = Strings.SampleData.DESCRIPTION_5
            )
        )
        return flowOf(dummyTransactions)
    }

    fun getTotalBalance(): Flow<Double> {
        return flowOf(24000.0)
    }

    fun getMonthlyIncome(): Flow<Double> {
        return flowOf(42500.0)
    }

    fun getMonthlyExpenses(): Flow<Double> {
        return flowOf(18200.0)
    }

    fun getNetProfit(): Flow<Double> {
        return flowOf(24300.0)
    }
}
