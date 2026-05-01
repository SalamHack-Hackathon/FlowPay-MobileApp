package com.abdallamusa.flowpay.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdallamusa.flowpay.ui.theme.CardBackground
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary
import com.abdallamusa.flowpay.ui.theme.SurfaceDark
import com.abdallamusa.flowpay.ui.theme.TextPrimary
import com.abdallamusa.flowpay.ui.theme.TextSecondary

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector?,
    iconPaint: Painter? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
    onIconClick: (() -> Unit)? = null,
    iconEnabled: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                color = CardBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = TextSecondary.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val iconModifier = Modifier
                .size(44.dp)
                .background(
                    color = EmeraldPrimary,
                    shape = RoundedCornerShape(10.dp)
                )
                .then(
                    if (onIconClick != null) {
                        Modifier.clickable(enabled = iconEnabled, onClick = onIconClick)
                    } else {
                        Modifier
                    }
                )

            Box(
                modifier = iconModifier,
                contentAlignment = Alignment.Center
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = SurfaceDark,
                        modifier = Modifier.size(20.dp)
                    )
                } ?: iconPaint?.let {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        tint = SurfaceDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp),
                singleLine = true,
                keyboardOptions = keyboardOptions,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.End
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = TextSecondary,
                                fontSize = 14.sp,
                                textAlign = TextAlign.End
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}
