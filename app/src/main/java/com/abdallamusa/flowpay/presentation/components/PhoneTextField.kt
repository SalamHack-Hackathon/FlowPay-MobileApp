package com.abdallamusa.flowpay.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdallamusa.flowpay.utils.Country
import com.abdallamusa.flowpay.utils.CountryUtils

@Composable
fun PhoneTextField(
    value: String,
    onValueChange: (String) -> Unit,
    selectedCountry: Country,
    onCountrySelected: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // Figma Colors
    val FieldBackground = Color(0xFF272B29)
    val CountryCodeBackground = Color(0xFF323734) // Slightly different color for the code area
    val BorderColor = Color(0xFF404944)
    val TextColor = Color(0xFFFFFFFF)
    val HintColor = Color(0xFFBFC9C3).copy(alpha = 0.4f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            // The border encompasses the entire row
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            // Clip ensures the inner backgrounds respect the rounded corners
            .clip(RoundedCornerShape(12.dp))
            .background(FieldBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 1. Country Code Dropdown Section (Appears on the Right in RTL)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(CountryCodeBackground)
                .clickable { expanded = true }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Select Country",
                    tint = HintColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = selectedCountry.dialCode,
                    color = TextColor,
                    fontSize = 15.sp,
                    // Ensure the plus sign and numbers format properly
                    style = TextStyle(textDirection = TextDirection.Ltr)
                )
            }

            // The actual dropdown menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(FieldBackground)
            ) {
                CountryUtils.getAllCountries.forEach { country ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "${country.nameArabic} (${country.dialCode})",
                                color = TextColor
                            )
                        },
                        onClick = {
                            onCountrySelected(country)
                            expanded = false
                        }
                    )
                }
            }
        }

        // 2. Vertical Divider
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(BorderColor)
        )

        // 3. Phone Number Input Section (Appears on the Left in RTL)
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                textStyle = TextStyle(
                    color = TextColor,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start,
                    textDirection = TextDirection.Ltr // Keeps phone numbers typing left-to-right
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = "5X XXX XXXX",
                                color = HintColor,
                                fontSize = 15.sp,
                                style = TextStyle(textDirection = TextDirection.Ltr)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // The Phone Icon (pushed to the far left side in RTL)
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = HintColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}