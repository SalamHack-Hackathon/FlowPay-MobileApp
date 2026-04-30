package com.abdallamusa.flowpay.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.KeyboardType
import com.abdallamusa.flowpay.ui.theme.ErrorColor

// Figma Selection Colors
private val FieldBackground = Color(0xFF272B29) // Darker inner field matching design
private val BorderColor = Color(0xFF404944) // Distinct border color
private val IconColor = Color(0xFFBFC9C3) // Light gray for icons
private val TextColor = Color(0xFFFFFFFF)
private val HintColor = Color(0xFFBFC9C3).copy(alpha = 0.4f)

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp) // Slightly taller to match Figma proportions
            .background(
                color = FieldBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = if (isError) ErrorColor else BorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "Leading" Icon (Will appear on the right in RTL)
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = IconColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Text input area
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                textStyle = TextStyle(
                    color = TextColor,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = HintColor,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            // "Trailing" Icon (Will appear on the left in RTL, usually for password visibility)
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = IconColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            enabled = onTrailingIconClick != null,
                            onClick = { onTrailingIconClick?.invoke() }
                        )
                )
            }
        }
    }
}

// --- Previews ---

@Preview(showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldEmailEmptyPreview() {
    AuthTextField(
        value = "",
        onValueChange = {},
        placeholder = "name@example.com",
        leadingIcon = Icons.Default.Email
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldEmailFilledPreview() {
    AuthTextField(
        value = "user@example.com",
        onValueChange = {},
        placeholder = "name@example.com",
        leadingIcon = Icons.Default.Email
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldPasswordHiddenPreview() {
    AuthTextField(
        value = "secret123",
        onValueChange = {},
        placeholder = "••••••••",
        leadingIcon = Icons.Default.Lock,
        trailingIcon = Icons.Default.VisibilityOff,
        onTrailingIconClick = {},
        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldPasswordVisiblePreview() {
    AuthTextField(
        value = "secret123",
        onValueChange = {},
        placeholder = "••••••••",
        leadingIcon = Icons.Default.Lock,
        trailingIcon = Icons.Default.Visibility,
        onTrailingIconClick = {},
        visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldPasswordEmptyPreview() {
    AuthTextField(
        value = "",
        onValueChange = {},
        placeholder = "••••••••",
        leadingIcon = Icons.Default.Lock,
        trailingIcon = Icons.Default.VisibilityOff,
        onTrailingIconClick = {}
    )
}

@Preview(name = "Full Name - Empty", showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldFullNameEmptyPreview() {
    AuthTextField(
        value = "",
        onValueChange = {},
        placeholder = "أدخل اسمك الكامل",
        leadingIcon = Icons.Default.Person
    )
}

@Preview(name = "Full Name - Filled", showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldFullNameFilledPreview() {
    AuthTextField(
        value = "أحمد محمد",
        onValueChange = {},
        placeholder = "أدخل اسمك الكامل",
        leadingIcon = Icons.Default.Person
    )
}

@Preview(name = "Phone - Empty", showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldPhoneEmptyPreview() {
    AuthTextField(
        value = "",
        onValueChange = {},
        placeholder = "+966 5X XXX XXXX",
        leadingIcon = Icons.Default.Phone,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
}

@Preview(name = "Phone - Filled", showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldPhoneFilledPreview() {
    AuthTextField(
        value = "+966 555 123 456",
        onValueChange = {},
        placeholder = "+966 5X XXX XXXX",
        leadingIcon = Icons.Default.Phone,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
}

@Preview(name = "Confirm Password - Empty", showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldConfirmPasswordEmptyPreview() {
    AuthTextField(
        value = "",
        onValueChange = {},
        placeholder = "••••••••",
        leadingIcon = Icons.Default.Lock,
        trailingIcon = Icons.Default.VisibilityOff,
        onTrailingIconClick = {}
    )
}

@Preview(name = "Confirm Password - Filled Hidden", showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldConfirmPasswordHiddenPreview() {
    AuthTextField(
        value = "secret123",
        onValueChange = {},
        placeholder = "••••••••",
        leadingIcon = Icons.Default.Lock,
        trailingIcon = Icons.Default.VisibilityOff,
        onTrailingIconClick = {},
        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
    )
}

@Preview(name = "Confirm Password - Filled Visible", showBackground = true, backgroundColor = 0xFF1E211F)
@Composable
private fun AuthTextFieldConfirmPasswordVisiblePreview() {
    AuthTextField(
        value = "secret123",
        onValueChange = {},
        placeholder = "••••••••",
        leadingIcon = Icons.Default.Lock,
        trailingIcon = Icons.Default.Visibility,
        onTrailingIconClick = {},
        visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None
    )
}