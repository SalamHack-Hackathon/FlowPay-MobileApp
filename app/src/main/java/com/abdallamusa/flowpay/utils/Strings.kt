package com.abdallamusa.flowpay.utils

object Strings {
    // App
    const val APP_NAME = "FlowPay"
    const val APP_INITIAL = "F"

    // Auth
    object Auth {
        const val WELCOME_BACK = "أهلاً بك مجدداً في محفظتك الرقمية"
        const val REGISTER_WELCOME = "انضم إلى النخبة واستمتع بتجربة مالية استثنائية."
        const val CREATE_NEW_ACCOUNT = "إنشاء حساب جديد"
        const val FULL_NAME = "الاسم الكامل"
        const val FULL_NAME_HINT = "أدخل اسمك الكامل"
        const val PHONE_NUMBER = "رقم الهاتف"
        const val PHONE_NUMBER_HINT = "5XXXX XXXX"
        const val COUNTRY_CODE = "+966"
        const val EMAIL = "البريد الإلكتروني"
        const val EMAIL_HINT = "example@domain.com"
        const val PASSWORD = "كلمة المرور"
        const val PASSWORD_HINT = "••••••••"
        const val CONFIRM_PASSWORD = "تأكيد كلمة المرور"
        const val CONFIRM_PASSWORD_HINT = "••••••••"
        const val FORGOT_PASSWORD = "نسيت كلمة المرور؟"
        const val LOGIN = "تسجيل الدخول"
        const val CREATE_ACCOUNT = "إنشاء الحساب"
        const val NO_ACCOUNT = "ليس لديك حساب؟ سجل الآن"
        const val HAS_ACCOUNT = "لديك حساب بالفعل؟ تسجيل الدخول"

        // Content Descriptions
        const val FULL_NAME_DESC = "Full Name"
        const val PHONE_DESC = "Phone"
        const val EMAIL_DESC = "Email"
        const val PASSWORD_DESC = "Password"
        const val CONFIRM_PASSWORD_DESC = "Confirm Password"
        const val TOGGLE_PASSWORD = "Toggle password visibility"
    }

    // Dashboard
    object Dashboard {
        const val GOOD_MORNING = "صباح الخير"
        const val WELCOME_USER = "أهلاً بك"
        const val TOTAL_BALANCE = "الرصيد الإجمالي"
        const val RECEIVE = "استلام"
        const val SEND = "إرسال"
        const val MONTHLY_INCOME = "الدخل الشهري"
        const val MONTHLY_EXPENSES = "المصروفات الشهرية"
        const val NET_PROFIT = "صافي الربح"
        const val CASH_FLOW = "التدفق النقدي"
        const val LAST_SIX_MONTHS = "آخر 6 أشهر"
        const val PENDING_BILLS = "فواتير معلقة"
        const val VIEW_ALL = "الكل"

        // Content Descriptions
        const val NOTIFICATIONS = "Notifications"
        const val PROFILE = "Profile"
    }

    // Transaction Status
    object TransactionStatus {
        const val PENDING = "قيد الانتظار"
        const val CONFIRMED = "تأكيد الدفع"
        const val COMPLETED = "مكتمل"
        const val FAILED = "فشل"
    }

    // Months
    object Months {
        const val JANUARY = "يناير"
        const val FEBRUARY = "فبراير"
        const val MARCH = "مارس"
        const val APRIL = "أبريل"
        const val MAY = "مايو"
        const val JUNE = "يونيو"
    }

    // Common
    object Common {
        const val CURRENCY = "SAR"
        const val CURRENCY_FORMAT = "%,.0f"
    }

    // AI Invoice
    object AiInvoice {
        const val TITLE = "اكتب إلى FlowPay لإنشاء فاتورة..."
        const val EXAMPLE = "مثال: \"فاتورة لأحمد بقيمة 5000 مقابل تصميم الشعار\""
        const val INPUT_HINT = "اكتب تفاصيل الفاتورة هنا...."
        const val PREVIEW_TITLE = "معاينة الفاتورة"
        const val SMART_DRAFT = "مسودة ذكية"
        const val CLIENT = "العميل"
        const val AMOUNT = "المبلغ"
        const val DESCRIPTION = "الوصف"
        const val CONFIRM_SEND = "تأكيد وإرسال"
        const val MANUAL_EDIT = "تعديل يدوي"
    }

    // Create Invoice
    object CreateInvoice {
        const val TITLE = "إنشاء فاتورة"
        const val CLIENT_NAME = "اسم العميل"
        const val CLIENT_NAME_HINT = "أدخل اسم العميل"
        const val AMOUNT = "المبلغ"
        const val AMOUNT_HINT = "أدخل المبلغ"
        const val SERVICE = "الخدمة"
        const val SERVICE_HINT = "أدخل نوع الخدمة"
        const val CREATE = "إنشاء الفاتورة"
    }

    // Expense Tracker
    object Expense {
        const val TITLE = "تتبع المصروفات"
        const val EXPENSE_NAME = "اسم المصروف"
        const val EXPENSE_NAME_HINT = "أدخل اسم المصروف"
        const val AMOUNT = "المبلغ"
        const val AMOUNT_HINT = "أدخل المبلغ"
        const val CATEGORY = "الفئة"
        const val CATEGORY_HINT = "اختر الفئة"
        const val ADD_EXPENSE = "إضافة المصروف"

        // Categories
        const val CATEGORY_FOOD = "طعام ومشروبات"
        const val CATEGORY_TRANSPORT = "نقل ومواصلات"
        const val CATEGORY_ENTERTAINMENT = "ترفيه"
        const val CATEGORY_SHOPPING = "تسوق"
        const val CATEGORY_BILLS = "فواتير"
        const val CATEGORY_HEALTH = "صحة"
        const val CATEGORY_OTHER = "أخرى"
    }

    // Bottom Navigation
    object Navigation {
        const val HOME = "الرئيسية"
        const val PAYMENTS = "المدفوعات"
        const val INVOICES = "الفواتير"
        const val AI = "الذكاء الاصطناعي"
        const val SETTINGS = "الإعدادات"
    }

    // Navigation Routes
    object Routes {
        const val SPLASH = "splash"
        const val AUTH = "auth"
        const val DASHBOARD = "dashboard"
        const val CREATE_INVOICE = "create_invoice"
        const val AI_INVOICE = "ai_invoice"
        const val EXPENSE_TRACKER = "expense_tracker"
    }

    // Sample Data (for demo purposes)
    object SampleData {
        const val COMPANY_1 = "شركة التقنية المتقدمة"
        const val COMPANY_2 = "مؤسسة الأفق"
        const val COMPANY_3 = "شركة النمو الرقمي"
        const val COMPANY_4 = "مجموعة الإبداع"
        const val COMPANY_5 = "شركة الاستشارات المالية"

        const val DESCRIPTION_1 = "خدمات تطوير البرمجيات"
        const val DESCRIPTION_2 = "تصميم الهوية البصرية"
        const val DESCRIPTION_3 = "التسويق الرقمي"
        const val DESCRIPTION_4 = "تطوير تطبيق الجوال"
        const val DESCRIPTION_5 = "استشارات مالية"
    }
    object AiChat {
        const val TITLE = "FlowAI"
        const val ONLINE = "متصل الآن"
        const val TODAY = "اليوم"
        const val INPUT_HINT = "اكتب رسالتك لـ FlowAI..."
        const val WELCOME_MESSAGE = "مرحباً أحمد، أنا مساعدك المالي الذكي من FlowPay. لاحظت وجود بعض التغييرات في نمط إنفاقك هذا الأسبوع. كيف يمكنني مساعدتك اليوم؟"
        const val EXPENSE_ANALYSIS = "تحليل المصروفات"
        const val LATEST_TRANSFERS = "آخر التحويلات"
    }

    // Clients
    object Clients {
        const val TITLE = "العملاء"
        const val SEARCH_HINT = "البحث عن عميل أو شركة..."
        const val PAID = "مدفوع"
        const val PENDING = "بانتظار الدفع"
        const val DUE_DATE = "تاريخ الاستحقاق"
        const val AMOUNT_DUE = "المبلغ المستحق"
        const val CONFIRM_PAYMENT = "تأكيد الدفع"
        const val DETAILS = "التفاصيل"
        const val COMPLETED = "مكتمل"
        const val LAST_PAYMENT = "آخر دفعة"
        const val TOTAL_TRANSACTIONS = "إجمالي التعاملات"
    }

    // Reports
    object Reports {
        const val TITLE = "التقارير والأداء"
        const val SUBTITLE = "نظرة شاملة على الأداء المالي لشهر أكتوبر ۲۰۲۳"
        const val CURRENT_MONTH = "الشهر الحالي"
        const val TOTAL_NET_PROFIT = "صافي الربح الإجمالي"
        const val LAST_MONTH = "عن الشهر الماضي"
        const val FINANCIAL_RATING = "درجة التقييم المالي"
        const val EXCELLENT_PERFORMANCE = "أداء ممتاز"
        const val INCOME_VS_EXPENSE = "الدخل مقابل النفقات"
        const val CASH_FLOW_ANALYSIS = "تحليل التدفق النقدي للأشهر السنة الماضية"
        const val INCOME = "الدخل"
        const val EXPENSE = "النفقات"
        const val TOP_SERVICE_CATEGORIES = "أعلى فئات الخدمات"
        const val WEALTH_MANAGEMENT = "إدارة الثروات"
        const val TAX_CONSULTING = "الاستشارات الضريبية"
        const val FINANCIAL_PLANNING = "التخطيط المالي"
    }

    // Arabic Months
    object ArabicMonths {
        const val OCTOBER = "أكتوبر"
        const val SEPTEMBER = "سبتمبر"
        const val AUGUST = "أغسطس"
        const val JULY = "يوليو"
        const val JUNE = "يونيو"
        const val MAY = "مايو"
    }
}
