package com.abdallamusa.flowpay.di

import com.abdallamusa.flowpay.domain.usecase.AddExpenseUseCase
import com.abdallamusa.flowpay.domain.usecase.CreateInvoiceUseCase
import com.abdallamusa.flowpay.domain.usecase.GenerateAiInvoiceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCreateInvoiceUseCase(
        invoiceRepository: com.abdallamusa.flowpay.domain.repository.InvoiceRepository
    ): CreateInvoiceUseCase {
        return CreateInvoiceUseCase(invoiceRepository)
    }

    @Provides
    @Singleton
    fun provideGenerateAiInvoiceUseCase(): GenerateAiInvoiceUseCase {
        return GenerateAiInvoiceUseCase()
    }

    @Provides
    @Singleton
    fun provideAddExpenseUseCase(
        expenseRepository: com.abdallamusa.flowpay.domain.repository.ExpenseRepository
    ): AddExpenseUseCase {
        return AddExpenseUseCase(expenseRepository)
    }
}
