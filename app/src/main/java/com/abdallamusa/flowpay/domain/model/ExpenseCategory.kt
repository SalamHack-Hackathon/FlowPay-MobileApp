package com.abdallamusa.flowpay.domain.model

enum class ExpenseCategory {
    FOOD,
    TRANSPORT,
    ENTERTAINMENT,
    BILLS,
    OTHER
}

fun ExpenseCategory.displayName(): String {
    return when (this) {
        ExpenseCategory.FOOD -> "طعام"
        ExpenseCategory.TRANSPORT -> "نقل"
        ExpenseCategory.ENTERTAINMENT -> "ترفيه"
        ExpenseCategory.BILLS -> "فواتير"
        ExpenseCategory.OTHER -> "أخرى"
    }
}
