package com.abdallamusa.flowpay.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.abdallamusa.flowpay.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.abdallamusa.flowpay.presentation.components.AuthTextField
import com.abdallamusa.flowpay.utils.Country
import com.abdallamusa.flowpay.presentation.components.PhoneTextField
import com.abdallamusa.flowpay.utils.CountryUtils
import com.abdallamusa.flowpay.ui.theme.AuthButtonBackground
import com.abdallamusa.flowpay.ui.theme.AuthButtonTextColor
import com.abdallamusa.flowpay.ui.theme.AuthCardBackground
import com.abdallamusa.flowpay.ui.theme.AuthCardBorderColor
import com.abdallamusa.flowpay.ui.theme.AuthHighlightColor
import com.abdallamusa.flowpay.ui.theme.AuthLabelColor
import com.abdallamusa.flowpay.ui.theme.ErrorColor
import com.abdallamusa.flowpay.ui.theme.ScreenBackground
import com.abdallamusa.flowpay.utils.ValidationUtils
import com.abdallamusa.flowpay.utils.ValidationUtils.getError
import com.abdallamusa.flowpay.utils.ValidationUtils.isValid

@Composable
fun AuthScreenContent(
    isLoginMode: Boolean,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isConfirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onLoginModeToggle: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    selectedCountry: Country,
    onCountrySelected: (Country) -> Unit,
    // Error states
    emailError: String? = null,
    passwordError: String? = null,
    confirmPasswordError: String? = null,
    fullNameError: String? = null,
    phoneError: String? = null
) {
    // Force RTL direction for Arabic Layout
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground)
                .padding(horizontal = 24.dp)
                // Added scroll state so smaller screens don't cut off the form
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // --- OUTSIDE CARD (TOP) ---

            // FlowPay Logo
            Text(
                text = stringResource(R.string.auth_login_title),
                color = AuthHighlightColor,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            // Login Subtitle (Only shows in login mode)
            if (isLoginMode) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.auth_login_subtitle),
                    color = AuthLabelColor,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- THE MAIN CARD ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = AuthCardBackground,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = AuthCardBorderColor,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Register Header & Subtitle (Only shows in register mode, INSIDE the card)
                if (!isLoginMode) {
                    Text(
                        text = stringResource(R.string.auth_register_title),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.auth_register_subtitle),
                        fontSize = 14.sp,
                        color = AuthLabelColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // Full Name Field (Sign Up Only)
                AnimatedVisibility(visible = !isLoginMode) {
                    Column {
                        Text(
                            text = stringResource(R.string.auth_full_name),
                            fontSize = 14.sp,
                            color = AuthLabelColor,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AuthTextField(
                            value = fullName,
                            onValueChange = onFullNameChange,
                            placeholder = stringResource(R.string.auth_full_name_hint),
                            leadingIcon = Icons.Default.Person,
                            isError = fullNameError != null
                        )
                        if (fullNameError != null) {
                            Text(
                                text = fullNameError,
                                color = ErrorColor,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Email Field
                Text(
                    text = stringResource(R.string.auth_email),
                    fontSize = 14.sp,
                    color = AuthLabelColor,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                AuthTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = stringResource(R.string.auth_email_hint),
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = emailError != null
                )
                if (emailError != null) {
                    Text(
                        text = emailError,
                        color = ErrorColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Phone Field (Sign Up Only) - Using our custom PhoneTextField
                AnimatedVisibility(visible = !isLoginMode) {
                    Column {
                        Text(
                            text = stringResource(R.string.auth_phone),
                            fontSize = 14.sp,
                            color = AuthLabelColor,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PhoneTextField(
                            value = phoneNumber,
                            onValueChange = onPhoneNumberChange,
                            selectedCountry = selectedCountry,
                            onCountrySelected = onCountrySelected
                        )
                        if (phoneError != null) {
                            Text(
                                text = phoneError,
                                color = ErrorColor,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Password Field Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.auth_password),
                        fontSize = 14.sp,
                        color = AuthLabelColor
                    )
                    // Forgot Password (Login Only)
                    if (isLoginMode) {
                        TextButton(
                            onClick = { /* Handle Forgot Password */ },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.height(24.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.auth_forgot_password),
                                color = AuthLabelColor,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                AuthTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = if (isLoginMode) stringResource(R.string.auth_password_hint_login) else stringResource(R.string.auth_password_hint_register),
                    leadingIcon = Icons.Default.Lock,
                    trailingIcon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    onTrailingIconClick = onTogglePasswordVisibility,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordError != null
                )
                if (passwordError != null) {
                    Text(
                        text = passwordError,
                        color = ErrorColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password (Sign Up Only)
                AnimatedVisibility(visible = !isLoginMode) {
                    Column {
                        Text(
                            text = stringResource(R.string.auth_confirm_password),
                            fontSize = 14.sp,
                            color = AuthLabelColor,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AuthTextField(
                            value = confirmPassword,
                            onValueChange = onConfirmPasswordChange,
                            placeholder = stringResource(R.string.auth_confirm_password_hint),
                            leadingIcon = Icons.Default.Lock,
                            trailingIcon = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            onTrailingIconClick = onToggleConfirmPasswordVisibility,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            isError = confirmPasswordError != null
                        )
                        if (confirmPasswordError != null) {
                            Text(
                                text = confirmPasswordError,
                                color = ErrorColor,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                if (isLoginMode) Spacer(modifier = Modifier.height(8.dp))

                // Main Action Button
                Button(
                    onClick = if (isLoginMode) onLoginClick else onRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AuthButtonBackground),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isLoginMode) stringResource(R.string.auth_login_button) else stringResource(R.string.auth_register_button),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AuthButtonTextColor
                    )

                    if (isLoginMode) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = AuthButtonTextColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Register Mode: Toggle Link is INSIDE the card at the bottom
                if (!isLoginMode) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onLoginModeToggle() }
                    ) {
                        Text(
                            text = stringResource(R.string.auth_has_account),
                            color = AuthLabelColor,
                            fontSize = 14.sp
                        )
                        Text(
                            text = stringResource(R.string.auth_login_now),
                            color = AuthHighlightColor,
                            fontSize = 14.sp
                        )
                    }
                }
            } // End of Card

            // --- OUTSIDE CARD (BOTTOM) ---

            // Login Mode: Toggle Link is OUTSIDE the card at the bottom
            if (isLoginMode) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onLoginModeToggle() }
                ) {
                    Text(
                        text = stringResource(R.string.auth_no_account),
                        color = AuthLabelColor,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(R.string.auth_register_now),
                        color = AuthHighlightColor,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit
) {
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(CountryUtils.getAllCountries.first()) }

    // Error states
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }

    // Clear errors when mode changes
    LaunchedEffect(isLoginMode) {
        emailError = null
        passwordError = null
        confirmPasswordError = null
        fullNameError = null
        phoneError = null
    }

    // Clear errors when input changes
    fun clearErrors() {
        emailError = null
        passwordError = null
        confirmPasswordError = null
        fullNameError = null
        phoneError = null
    }

    fun validateAndLogin(): Boolean {
        clearErrors()
        val emailResult = ValidationUtils.validateEmail(email)
        val passwordResult = ValidationUtils.validatePassword(password)

        var isValid = true
        if (!emailResult.isValid()) {
            emailError = emailResult.getError()
            isValid = false
        }
        if (!passwordResult.isValid()) {
            passwordError = passwordResult.getError()
            isValid = false
        }
        return isValid
    }

    fun validateAndRegister(): Boolean {
        clearErrors()
        val fullNameResult = ValidationUtils.validateFullName(fullName)
        val emailResult = ValidationUtils.validateEmail(email)
        val passwordResult = ValidationUtils.validateStrongPassword(password)
        val confirmResult = ValidationUtils.validateConfirmPassword(password, confirmPassword)
        val phoneResult = ValidationUtils.validatePhoneNumber(phoneNumber, selectedCountry.dialCode)

        var isValid = true
        if (!fullNameResult.isValid()) {
            fullNameError = fullNameResult.getError()
            isValid = false
        }
        if (!emailResult.isValid()) {
            emailError = emailResult.getError()
            isValid = false
        }
        if (!passwordResult.isValid()) {
            passwordError = passwordResult.getError()
            isValid = false
        }
        if (!confirmResult.isValid()) {
            confirmPasswordError = confirmResult.getError()
            isValid = false
        }
        if (!phoneResult.isValid()) {
            phoneError = phoneResult.getError()
            isValid = false
        }
        return isValid
    }

    AuthScreenContent(
        isLoginMode = isLoginMode,
        email = email,
        onEmailChange = { email = it; emailError = null },
        password = password,
        onPasswordChange = { password = it; passwordError = null },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it; confirmPasswordError = null },
        fullName = fullName,
        onFullNameChange = { fullName = it; fullNameError = null },
        phoneNumber = phoneNumber,
        onPhoneNumberChange = { phoneNumber = it; phoneError = null },
        isPasswordVisible = isPasswordVisible,
        onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
        isConfirmPasswordVisible = isConfirmPasswordVisible,
        onToggleConfirmPasswordVisibility = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
        onLoginModeToggle = { isLoginMode = !isLoginMode },
        onLoginClick = { if (validateAndLogin()) onLoginSuccess() },
        onRegisterClick = { if (validateAndRegister()) onLoginSuccess() },
        selectedCountry = selectedCountry,
        onCountrySelected = { selectedCountry = it },
        emailError = emailError,
        passwordError = passwordError,
        confirmPasswordError = confirmPasswordError,
        fullNameError = fullNameError,
        phoneError = phoneError
    )
}

// ==================== PREVIEW PROVIDER ====================

class AuthModeProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false) // true = Login, false = Sign Up
}

// ==================== PREVIEWS ====================

@Preview(name = "Auth Screen - Both Modes", showBackground = true, backgroundColor = 0xFF0A0C0B)
@Composable
private fun AuthScreenPreview(
    @PreviewParameter(AuthModeProvider::class) isLoginMode: Boolean
) {
    var email by remember { mutableStateOf(if (isLoginMode) "user@example.com" else "") }
    var password by remember { mutableStateOf(if (isLoginMode) "password123" else "") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(CountryUtils.getAllCountries.first()) }

    AuthScreenContent(
        isLoginMode = isLoginMode,
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        fullName = fullName,
        onFullNameChange = { fullName = it },
        phoneNumber = phoneNumber,
        onPhoneNumberChange = { phoneNumber = it },
        isPasswordVisible = isPasswordVisible,
        onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
        isConfirmPasswordVisible = isConfirmPasswordVisible,
        onToggleConfirmPasswordVisibility = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
        onLoginModeToggle = { },
        onLoginClick = { },
        onRegisterClick = { },
        selectedCountry = selectedCountry,
        onCountrySelected = { selectedCountry = it }
    )
}

@Preview(name = "Login - Empty State", showBackground = true, backgroundColor = 0xFF0A0C0B)
@Composable
private fun AuthScreenLoginEmptyPreview() {
    AuthScreenContent(
        isLoginMode = true,
        email = "",
        onEmailChange = { },
        password = "",
        onPasswordChange = { },
        confirmPassword = "",
        onConfirmPasswordChange = { },
        fullName = "",
        onFullNameChange = { },
        phoneNumber = "",
        onPhoneNumberChange = { },
        isPasswordVisible = false,
        onTogglePasswordVisibility = { },
        isConfirmPasswordVisible = false,
        onToggleConfirmPasswordVisibility = { },
        onLoginModeToggle = { },
        onLoginClick = { },
        onRegisterClick = { },
        selectedCountry = CountryUtils.getAllCountries.first(),
        onCountrySelected = { }
    )
}

@Preview(name = "Login - With Errors", showBackground = true, backgroundColor = 0xFF0A0C0B)
@Composable
private fun AuthScreenLoginErrorsPreview() {
    AuthScreenContent(
        isLoginMode = true,
        email = "invalid-email",
        onEmailChange = { },
        password = "123",
        onPasswordChange = { },
        confirmPassword = "",
        onConfirmPasswordChange = { },
        fullName = "",
        onFullNameChange = { },
        phoneNumber = "",
        onPhoneNumberChange = { },
        isPasswordVisible = false,
        onTogglePasswordVisibility = { },
        isConfirmPasswordVisible = false,
        onToggleConfirmPasswordVisibility = { },
        onLoginModeToggle = { },
        onLoginClick = { },
        onRegisterClick = { },
        selectedCountry = CountryUtils.getAllCountries.first(),
        onCountrySelected = { },
        emailError = "البريد الإلكتروني غير صالح",
        passwordError = "كلمة المرور يجب أن تكون 6 أحرف على الأقل"
    )
}

@Preview(name = "Sign Up - Filled State", showBackground = true, backgroundColor = 0xFF0A0C0B)
@Composable
private fun AuthScreenSignUpFilledPreview() {
    AuthScreenContent(
        isLoginMode = false,
        email = "ahmed@example.com",
        onEmailChange = { },
        password = "StrongPass123!",
        onPasswordChange = { },
        confirmPassword = "StrongPass123!",
        onConfirmPasswordChange = { },
        fullName = "أحمد محمد",
        onFullNameChange = { },
        phoneNumber = "555 123 456",
        onPhoneNumberChange = { },
        isPasswordVisible = false,
        onTogglePasswordVisibility = { },
        isConfirmPasswordVisible = false,
        onToggleConfirmPasswordVisibility = { },
        onLoginModeToggle = { },
        onLoginClick = { },
        onRegisterClick = { },
        selectedCountry = CountryUtils.getAllCountries.first { it.code == "SA" },
        onCountrySelected = { }
    )
}

@Preview(name = "Sign Up - With Errors", showBackground = true, backgroundColor = 0xFF0A0C0B)
@Composable
private fun AuthScreenSignUpErrorsPreview() {
    AuthScreenContent(
        isLoginMode = false,
        email = "",
        onEmailChange = { },
        password = "weak",
        onPasswordChange = { },
        confirmPassword = "different",
        onConfirmPasswordChange = { },
        fullName = "A",
        onFullNameChange = { },
        phoneNumber = "",
        onPhoneNumberChange = { },
        isPasswordVisible = false,
        onTogglePasswordVisibility = { },
        isConfirmPasswordVisible = false,
        onToggleConfirmPasswordVisibility = { },
        onLoginModeToggle = { },
        onLoginClick = { },
        onRegisterClick = { },
        selectedCountry = CountryUtils.getAllCountries.first(),
        onCountrySelected = { },
        fullNameError = "الاسم يجب أن يكون 3 أحرف على الأقل",
        emailError = "البريد الإلكتروني مطلوب",
        passwordError = "كلمة المرور يجب أن تكون 8 أحرف على الأقل",
        confirmPasswordError = "كلمتا المرور غير متطابقتين"
    )
}