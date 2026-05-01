package com.abdallamusa.flowpay.utils

object ValidationUtils {

    sealed class ValidationResult {
        data object Valid : ValidationResult()
        data class Invalid(val errorMessage: String) : ValidationResult()
    }

    // ==================== Full Name Validation ====================
    fun validateFullName(fullName: String): ValidationResult {
        return when {
            fullName.isBlank() -> ValidationResult.Invalid("الاسم الكامل مطلوب")
            fullName.length < 4 -> ValidationResult.Invalid("الاسم يجب أن يكون 4 أحرف على الأقل")
            fullName.length > 50 -> ValidationResult.Invalid("الاسم طويل جداً")
            !fullName.matches(Regex("^[\u0600-\u06FFa-zA-Z\\s]+$")) -> 
                ValidationResult.Invalid("الاسم يجب أن يحتوي على حروف فقط")
            else -> ValidationResult.Valid
        }
    }

    // ==================== Email Validation ====================
    fun validateEmail(email: String): ValidationResult {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return when {
            email.isBlank() -> ValidationResult.Invalid("البريد الإلكتروني مطلوب")
            !email.matches(emailRegex) -> ValidationResult.Invalid("البريد الإلكتروني غير صالح")
            email.length > 100 -> ValidationResult.Invalid("البريد الإلكتروني طويل جداً")
            else -> ValidationResult.Valid
        }
    }

    // ==================== Strong Password Validation ====================
    fun validateStrongPassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid("كلمة المرور مطلوبة")
            password.length < 8 -> ValidationResult.Invalid("كلمة المرور يجب أن تكون 8 أحرف على الأقل")
            !password.any { it.isUpperCase() } -> 
                ValidationResult.Invalid("كلمة المرور يجب أن تحتوي على حرف كبير")
            !password.any { it.isLowerCase() } -> 
                ValidationResult.Invalid("كلمة المرور يجب أن تحتوي على حرف صغير")
            !password.any { it.isDigit() } -> 
                ValidationResult.Invalid("كلمة المرور يجب أن تحتوي على رقم")
            !password.any { !it.isLetterOrDigit() } -> 
                ValidationResult.Invalid("كلمة المرور يجب أن تحتوي على رمز خاص (!@#$%)")
            else -> ValidationResult.Valid
        }
    }

    // ==================== Simple Password Validation (Login) ====================
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid("كلمة المرور مطلوبة")
            password.length < 6 -> ValidationResult.Invalid("كلمة المرور يجب أن تكون 6 أحرف على الأقل")
            else -> ValidationResult.Valid
        }
    }

    // ==================== Phone Number Validation ====================
    fun validatePhoneNumber(phoneNumber: String, dialCode: String = "+966"): ValidationResult {
        // Remove all spaces and non-digit characters
        val cleanedNumber = phoneNumber.replace(Regex("[^\\d]"), "")

        return when {
            phoneNumber.isBlank() -> ValidationResult.Invalid("رقم الهاتف مطلوب")
            cleanedNumber.length < 8 -> ValidationResult.Invalid("رقم الهاتف قصير جداً")
            cleanedNumber.length > 12 -> ValidationResult.Invalid("رقم الهاتف طويل جداً")
            !cleanedNumber.matches(Regex("^\\d+$")) ->
                ValidationResult.Invalid("رقم الهاتف يجب أن يحتوي على أرقام فقط")
            else -> ValidationResult.Valid
        }
    }

    // ==================== Confirm Password Validation ====================
    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult.Invalid("تأكيد كلمة المرور مطلوب")
            confirmPassword != password -> ValidationResult.Invalid("كلمتا المرور غير متطابقتين")
            else -> ValidationResult.Valid
        }
    }

    // ==================== Numeric Amount Validation ====================
    fun validateAmount(amount: String): ValidationResult {
        return when {
            amount.isBlank() -> ValidationResult.Invalid("المبلغ مطلوب")
            !amount.matches(Regex("^\\d*\\.?\\d+$")) -> 
                ValidationResult.Invalid("المبلغ يجب أن يكون رقماً")
            amount.toDoubleOrNull() == null -> ValidationResult.Invalid("المبلغ غير صالح")
            amount.toDouble() <= 0 -> ValidationResult.Invalid("المبلغ يجب أن يكون أكبر من صفر")
            amount.toDouble() > 1000000000 -> ValidationResult.Invalid("المبلغ كبير جداً")
            else -> ValidationResult.Valid
        }
    }

    // ==================== Helper Extensions ====================
    fun ValidationResult.isValid(): Boolean = this is ValidationResult.Valid
    fun ValidationResult.getError(): String? = (this as? ValidationResult.Invalid)?.errorMessage

    // ==================== Input Filter Extensions ====================
    fun String.filterNumericInput(): String {
        return this.filter { it.isDigit() || it == '.' }
            .replace(Regex("^\\."), "0.")
            .replace(Regex("\\.(?=.*\\.)"), "")
    }
}
