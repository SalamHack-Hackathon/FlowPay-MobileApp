package com.abdallamusa.flowpay.data.repository

import com.abdallamusa.flowpay.data.local.AppDataStoreManager
import com.abdallamusa.flowpay.domain.model.ChatMessage
import com.abdallamusa.flowpay.domain.model.Expense
import com.abdallamusa.flowpay.domain.model.ExpenseCategory
import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.InvoiceStatus
import com.abdallamusa.flowpay.domain.model.Result
import com.abdallamusa.flowpay.domain.model.User
import com.abdallamusa.flowpay.domain.repository.FlowPayRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class FakeFlowPayRepositoryImpl @Inject constructor(
    private val gson: Gson,
    private val dataStoreManager: AppDataStoreManager
) : FlowPayRepository {

    // CoroutineScope for background DataStore operations
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    // MutableStateFlows for state management
    private val _currentUser = MutableStateFlow<User?>(null)
    private val _invoices = MutableStateFlow<List<Invoice>>(emptyList())
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    private val _userCurrency = MutableStateFlow<String>("ر.س")

    init {
        println("DEBUG: FakeFlowPayRepositoryImpl initialized - instance: ${this.hashCode()}")
    }

    // Public read-only StateFlows
    override val currentUser: Flow<User?> = _currentUser.asStateFlow()
    override val invoices: Flow<List<Invoice>> = _invoices.asStateFlow()
    override val expenses: Flow<List<Expense>> = _expenses.asStateFlow()
    override val chatMessages: Flow<List<ChatMessage>> = _chatMessages.asStateFlow()
    override val userCurrency: Flow<String> = _userCurrency.asStateFlow()

    // Derived Reactive Flows
    override val totalIncome: Flow<Double> = _invoices.map { invoices ->
        invoices.filter { it.status == InvoiceStatus.PAID }.sumOf { it.amount }
    }

    override val totalExpenses: Flow<Double> = _expenses.map { expenses ->
        expenses.sumOf { it.amount }
    }

    override val netProfit: Flow<Double> = combine(totalIncome, totalExpenses) { income, expenses ->
        income - expenses
    }

    override val financialScore: Flow<Int> = combine(totalIncome, totalExpenses) { income, expenses ->
        val total = income + expenses
        if (total == 0.0) {
            0
        } else {
            ((income / total) * 100).toInt()
        }
    }

    // Mutation Functions
    override suspend fun registerUser(user: User, password: String, currency: String) {
        // Save user credentials to DataStore
        dataStoreManager.saveUserCredentials(
            email = user.email,
            password = password,
            name = user.name,
            phone = user.phone,
            currency = currency
        )
        // Update in-memory state
        _currentUser.value = user
        _userCurrency.value = currency
    }

    override suspend fun loginUser(email: String, password: String): Result<User> {
        // CHECK 1: Does user exist?
        val storedEmail = dataStoreManager.getUserEmail()
        if (storedEmail.isNullOrEmpty()) {
            return Result.Error("لا يوجد حساب مسجل بهذا البريد. يرجى إنشاء حساب جديد.")
        }

        // CHECK 2: Do credentials match?
        val storedPassword = dataStoreManager.getUserPassword()
        if (storedEmail != email || storedPassword != password) {
            return Result.Error("بيانات الدخول غير صحيحة، يرجى التأكد من البريد الإلكتروني وكلمة المرور.")
        }

        // SUCCESS: Load user data and restore from DataStore
        val storedName = dataStoreManager.getUserName() ?: ""
        val storedPhone = dataStoreManager.getUserPhone() ?: ""
        val storedCurrency = dataStoreManager.getUserCurrency() ?: "ر.س"

        val user = User(
            id = email,
            name = storedName,
            email = email,
            phone = storedPhone,
            currency = storedCurrency
        )

        _currentUser.value = user
        _userCurrency.value = storedCurrency
        restoreUserData()

        return Result.Success(user)
    }

    override suspend fun addInvoice(invoice: Invoice) {
        // 1. Update State immediately for UI
        Log.d("FlowPayDebug", "addInvoice called. New list size: ${_invoices.value.size + 1}, client: ${invoice.clientName}")
        _invoices.value = _invoices.value + invoice
        Log.d("FlowPayDebug", "addInvoice completed. Total invoices: ${_invoices.value.size}")
        
        // 2. Save to DataStore in background
        repositoryScope.launch {
            saveInvoicesToDataStore()
        }
    }

    override suspend fun markInvoicePaid(invoiceId: String) {
        // 1. Update State immediately for UI
        _invoices.value = _invoices.value.map { invoice ->
            if (invoice.id == invoiceId) {
                invoice.copy(status = InvoiceStatus.PAID)
            } else {
                invoice
            }
        }
        
        // 2. Save to DataStore in background
        repositoryScope.launch {
            saveInvoicesToDataStore()
        }
    }

    override suspend fun addExpense(expense: Expense) {
        // 1. Update State immediately for UI
        _expenses.value = _expenses.value + expense
        
        // 2. Save to DataStore in background
        repositoryScope.launch {
            saveExpensesToDataStore()
        }
    }

    override suspend fun addChatMessage(message: ChatMessage) {
        // 1. Update State immediately for UI
        _chatMessages.value = _chatMessages.value + message
        
        // 2. Save to DataStore in background
        repositoryScope.launch {
            saveChatMessagesToDataStore()
        }
    }

    override suspend fun sendWelcomeMessageIfNeeded() {
        if (_chatMessages.value.isEmpty()) {
            val welcomeMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = "مرحباً بك في FlowPay! 🌟\nأنا مساعدك المالي الذكي. كيف يمكنني دعم أعمالك اليوم؟\n\nاختر رقماً للبدء:\n1️⃣ تحليل التدفق النقدي ومراجعة المصروفات.\n2️⃣ نصائح لزيادة الأرباح وتسعير خدماتك.\n3️⃣ مساعدة في الفواتير وإدارة العملاء.",
                isFromUser = false,
                timestamp = System.currentTimeMillis()
            )
            // Update State immediately
            _chatMessages.value += welcomeMessage
            // Save to DataStore in background
            repositoryScope.launch {
                saveChatMessagesToDataStore()
            }
        }
    }

    // Mock AI Functions
    override suspend fun getAiChatResponse(prompt: String) {
        // 1. Simulate network thinking time
        delay(1500)

        // 2. Simple state machine based on user input
        val cleanPrompt = prompt.trim()
        val responseText = when {
            cleanPrompt == "1" || cleanPrompt.contains("1️⃣") ->
                "ممتاز! بناءً على بياناتك الحالية، التدفق النقدي الخاص بك مستقر، ولكن أنصحك بمراجعة المصروفات المتكررة (مثل الاشتراكات). هل ترغب في أن نذهب معاً لتسجيل مصروف جديد الآن؟"

            cleanPrompt == "2" || cleanPrompt.contains("2️⃣") ->
                "التسعير الذكي هو مفتاح النمو! كبداية، جرب رفع أسعارك بنسبة 10% للعملاء الجدد، وركز على خدماتك الأكثر مبيعاً. هل تريدني أن أساعدك في صياغة رسالة احترافية لإخبار عملائك الحاليين بتحديث الأسعار؟"

            cleanPrompt == "3" || cleanPrompt.contains("3️⃣") ->
                "إدارة الفواتير هي تخصصي! لإنشاء فاتورة بسرعة، فقط اذهب لصفحة 'الفواتير الذكية' واكتب لي شيئاً مثل: (فاتورة لـ أحمد بـ 5000). أنا سأقوم بتجهيزها لك فوراً. هل لديك أي استفسار آخر حول العملاء؟"

            cleanPrompt.contains("نعم") || cleanPrompt.contains("أكيد") || cleanPrompt.contains("ok") ->
                "رائع جداً! يمكنك التنقل عبر القائمة السفلية للبدء في التنفيذ. وتذكر، أنا متواجد هنا دائماً إذا احتجت لأي تحليل مالي أو استشارة."

            cleanPrompt.contains("لا") || cleanPrompt.contains("شكرا") ->
                "العفو! أتمنى لك يوماً مليئاً بالأرباح والنجاح. لا تتردد في العودة إليّ عندما تحتاج إلى مساعدة."

            else ->
                "سؤال ممتاز! بصفتي مساعدك المالي، أنا أتعلم من بياناتك باستمرار. لمساعدتك بشكل أدق، يمكنك اختيار (1) أو (2) أو (3) من خياراتنا السابقة، أو سؤالي عن التدفق النقدي الخاص بك!"
        }

        // 3. Emit the AI response - update State immediately
        val aiMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = responseText,
            isFromUser = false,
            timestamp = System.currentTimeMillis()
        )
        _chatMessages.value = _chatMessages.value + aiMessage
        // Save to DataStore in background
        repositoryScope.launch {
            saveChatMessagesToDataStore()
        }
    }

    override suspend fun generateAiInvoice(prompt: String): Invoice {
        delay(1500) // Simulate AI thinking time
        
        // Extract amount (first sequence of digits)
        val amount = Regex("\\d+").find(prompt)?.value?.toDoubleOrNull() ?: 0.0
        
        // Extract client name (first word)
        val clientName = prompt.split("\\s+".toRegex()).firstOrNull() ?: "Unknown Client"
        
        return Invoice(
            id = UUID.randomUUID().toString(),
            clientName = clientName,
            amount = amount,
            service = "AI Generated",
            date = System.currentTimeMillis(),
            status = InvoiceStatus.DRAFT
        )
    }

    // Data Persistence Functions
    private suspend fun saveInvoicesToDataStore() {
        val json = gson.toJson(_invoices.value)
        dataStoreManager.saveInvoicesJson(json)
    }

    private suspend fun saveExpensesToDataStore() {
        val json = gson.toJson(_expenses.value)
        dataStoreManager.saveExpensesJson(json)
    }

    private suspend fun saveChatMessagesToDataStore() {
        val json = gson.toJson(_chatMessages.value)
        dataStoreManager.saveChatMessagesJson(json)
    }

    override suspend fun restoreUserData() {
        Log.d("FlowPayDebug", "restoreUserData started")
        
        // Restore Invoices with try-catch
        val invoicesJson = dataStoreManager.getInvoicesJson()
        Log.d("FlowPayDebug", "restoreUserData invoices JSON: $invoicesJson")
        val restoredInvoices: List<Invoice> = try {
            val invoiceType = object : TypeToken<List<Invoice>>() {}.type
            gson.fromJson(invoicesJson, invoiceType) ?: emptyList()
        } catch (e: Exception) {
            Log.e("FlowPayDebug", "JSON Parse Error for invoices", e)
            emptyList()
        }
        _invoices.value = restoredInvoices
        Log.d("FlowPayDebug", "restoreUserData restored ${restoredInvoices.size} invoices")

        // Restore Expenses with try-catch
        val expensesJson = dataStoreManager.getExpensesJson()
        Log.d("FlowPayDebug", "restoreUserData expenses JSON: $expensesJson")
        val restoredExpenses: List<Expense> = try {
            val expenseType = object : TypeToken<List<Expense>>() {}.type
            gson.fromJson(expensesJson, expenseType) ?: emptyList()
        } catch (e: Exception) {
            Log.e("FlowPayDebug", "JSON Parse Error for expenses", e)
            emptyList()
        }
        _expenses.value = restoredExpenses
        Log.d("FlowPayDebug", "restoreUserData restored ${restoredExpenses.size} expenses")

        // Restore Chat Messages with try-catch
        val chatJson = dataStoreManager.getChatMessagesJson()
        Log.d("FlowPayDebug", "restoreUserData chat JSON: $chatJson")
        val restoredChat: List<ChatMessage> = try {
            val chatType = object : TypeToken<List<ChatMessage>>() {}.type
            gson.fromJson(chatJson, chatType) ?: emptyList()
        } catch (e: Exception) {
            Log.e("FlowPayDebug", "JSON Parse Error for chat messages", e)
            emptyList()
        }
        _chatMessages.value = restoredChat
        Log.d("FlowPayDebug", "restoreUserData restored ${restoredChat.size} chat messages")

        // Restore User Credentials
        val storedEmail = dataStoreManager.getUserEmail()
        if (!storedEmail.isNullOrEmpty()) {
            val storedName = dataStoreManager.getUserName() ?: ""
            val storedPhone = dataStoreManager.getUserPhone() ?: ""
            val storedCurrency = dataStoreManager.getUserCurrency() ?: "ر.س"

            _currentUser.value = User(
                id = storedEmail,
                name = storedName,
                email = storedEmail,
                phone = storedPhone,
                currency = storedCurrency
            )
            _userCurrency.value = storedCurrency
            Log.d("FlowPayDebug", "restoreUserData restored user: $storedName")
        }
        Log.d("FlowPayDebug", "restoreUserData completed. Total invoices: ${_invoices.value.size}, expenses: ${_expenses.value.size}")
    }
}
