package com.abdallamusa.flowpay.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "flowpay_data")

@Singleton
class AppDataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val INVOICES_JSON = stringPreferencesKey("invoices_json")
        private val EXPENSES_JSON = stringPreferencesKey("expenses_json")
        private val CHAT_MESSAGES_JSON = stringPreferencesKey("chat_messages_json")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PASSWORD = stringPreferencesKey("user_password")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_PHONE = stringPreferencesKey("user_phone")
        private val USER_CURRENCY = stringPreferencesKey("user_currency")
    }

    suspend fun saveInvoicesJson(json: String) {
        context.dataStore.edit { preferences ->
            preferences[INVOICES_JSON] = json
        }
    }

    suspend fun getInvoicesJson(): String {
        return context.dataStore.data.first()[INVOICES_JSON] ?: "[]"
    }

    suspend fun saveExpensesJson(json: String) {
        context.dataStore.edit { preferences ->
            preferences[EXPENSES_JSON] = json
        }
    }

    suspend fun getExpensesJson(): String {
        return context.dataStore.data.first()[EXPENSES_JSON] ?: "[]"
    }

    suspend fun saveChatMessagesJson(json: String) {
        context.dataStore.edit { preferences ->
            preferences[CHAT_MESSAGES_JSON] = json
        }
    }

    suspend fun getChatMessagesJson(): String {
        return context.dataStore.data.first()[CHAT_MESSAGES_JSON] ?: "[]"
    }

    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // User Credential Storage
    suspend fun saveUserCredentials(email: String, password: String, name: String, phone: String, currency: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
            preferences[USER_PASSWORD] = password
            preferences[USER_NAME] = name
            preferences[USER_PHONE] = phone
            preferences[USER_CURRENCY] = currency
        }
    }

    suspend fun getUserEmail(): String? {
        return context.dataStore.data.first()[USER_EMAIL]
    }

    suspend fun getUserPassword(): String? {
        return context.dataStore.data.first()[USER_PASSWORD]
    }

    suspend fun getUserName(): String? {
        return context.dataStore.data.first()[USER_NAME]
    }

    suspend fun getUserPhone(): String? {
        return context.dataStore.data.first()[USER_PHONE]
    }

    suspend fun getUserCurrency(): String? {
        return context.dataStore.data.first()[USER_CURRENCY]
    }

    suspend fun hasRegisteredUser(): Boolean {
        val email = context.dataStore.data.first()[USER_EMAIL]
        val result = !email.isNullOrEmpty()
        android.util.Log.d("FlowPayDebug", "AppDataStoreManager.hasRegisteredUser() - email: '$email', result: $result")
        return result
    }

    suspend fun clearUserCredentials() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_PASSWORD)
            preferences.remove(USER_NAME)
            preferences.remove(USER_PHONE)
            preferences.remove(USER_CURRENCY)
        }
    }
}
