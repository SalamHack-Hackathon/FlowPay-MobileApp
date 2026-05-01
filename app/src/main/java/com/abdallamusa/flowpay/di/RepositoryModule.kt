package com.abdallamusa.flowpay.di

import com.abdallamusa.flowpay.data.repository.ExpenseRepositoryImpl
import com.abdallamusa.flowpay.data.repository.FakeFlowPayRepositoryImpl
import com.abdallamusa.flowpay.data.repository.InvoiceRepositoryImpl
import com.abdallamusa.flowpay.domain.repository.ExpenseRepository
import com.abdallamusa.flowpay.domain.repository.FlowPayRepository
import com.abdallamusa.flowpay.domain.repository.InvoiceRepository
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindInvoiceRepository(
        invoiceRepositoryImpl: InvoiceRepositoryImpl
    ): InvoiceRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        expenseRepositoryImpl: ExpenseRepositoryImpl
    ): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindFlowPayRepository(
        fakeFlowPayRepositoryImpl: FakeFlowPayRepositoryImpl
    ): FlowPayRepository

    companion object {
        @Provides
        @Singleton
        fun provideGson(): Gson {
            return Gson()
        }
    }
}
