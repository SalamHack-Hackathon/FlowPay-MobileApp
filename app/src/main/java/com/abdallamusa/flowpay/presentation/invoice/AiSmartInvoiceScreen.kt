package com.abdallamusa.flowpay.presentation.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.abdallamusa.flowpay.R
import com.abdallamusa.flowpay.presentation.viewmodel.AiSmartInvoiceViewModel
import com.abdallamusa.flowpay.presentation.viewmodel.AiInvoiceUiState
import com.abdallamusa.flowpay.utils.Strings
import com.abdallamusa.flowpay.domain.usecase.GenerateAiInvoiceUseCase
import com.abdallamusa.flowpay.presentation.components.CustomTextField
import com.abdallamusa.flowpay.presentation.components.FlowPayTopAppBar
import com.abdallamusa.flowpay.presentation.components.TopBarStyle
import com.abdallamusa.flowpay.presentation.dashboard.glassCard
import com.abdallamusa.flowpay.ui.theme.BackgroundDark
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary
import com.abdallamusa.flowpay.ui.theme.TextPrimary
import com.abdallamusa.flowpay.ui.theme.TextSecondary
import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.InvoiceStatus
import com.abdallamusa.flowpay.ui.theme.SurfaceDark
import java.util.Locale

@Composable
fun AiSmartInvoiceScreen(
    viewModel: AiSmartInvoiceViewModel? = null,
    onInvoiceSent: () -> Unit,
    previewUiState: AiInvoiceUiState? = null
) {
    val isPreview = LocalInspectionMode.current
    val actualViewModel = if (isPreview) null else (viewModel ?: hiltViewModel())
    var prompt by remember { mutableStateOf("") }
    val uiState by (previewUiState?.let { remember { mutableStateOf(it) } } 
        ?: actualViewModel?.uiState?.collectAsState() 
        ?: remember { mutableStateOf(AiInvoiceUiState()) })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FlowPayTopAppBar(style = TopBarStyle.LogoWithActions)
        }
    ) { paddingValues ->
        // Observe successful save and navigate
        LaunchedEffect(uiState.isSaved) {
            if (uiState.isSaved) {
                onInvoiceSent()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .padding(paddingValues)
                .padding(24.dp)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = Strings.AiInvoice.TITLE,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = Strings.AiInvoice.EXAMPLE,
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                value = prompt,
                onValueChange = { prompt = it },
                placeholder = Strings.AiInvoice.INPUT_HINT,
                icon = null,
                iconPaint = painterResource(id = R.drawable.send),
                onIconClick = {
                    if (prompt.isNotBlank()) {
                        actualViewModel?.generateInvoice(prompt)
                    }
                },
                iconEnabled = prompt.isNotBlank() && !uiState.isLoading
            )

        if (uiState.isLoading) {
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(color = EmeraldPrimary)
        }

        uiState.generatedInvoice?.let { invoice ->
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCard(),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Strings.AiInvoice.PREVIEW_TITLE,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Surface(
                            color = EmeraldPrimary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = Strings.AiInvoice.SMART_DRAFT,
                                fontSize = 12.sp,
                                color = EmeraldPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    InvoiceDetailRow(
                        label = Strings.AiInvoice.CLIENT,
                        value = invoice.clientName
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InvoiceDetailRow(
                        label = Strings.AiInvoice.AMOUNT,
                        value = "${Strings.Common.CURRENCY} ${String.format(Locale.US, Strings.Common.CURRENCY_FORMAT, invoice.amount)}"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InvoiceDetailRow(
                        label = Strings.AiInvoice.DESCRIPTION,
                        value = invoice.service
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                actualViewModel?.confirmInvoice()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = Strings.AiInvoice.CONFIRM_SEND,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BackgroundDark
                            )
                        }

                        TextButton(
                            onClick = { actualViewModel?.clearInvoice() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = Strings.AiInvoice.MANUAL_EDIT,
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        uiState.error?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = androidx.compose.ui.graphics.Color.Red,
                fontSize = 14.sp
            )
        }
        }
    }
}

@Composable
fun InvoiceDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
    }
}




@Preview(showBackground = true)
@Composable
fun AiSmartInvoiceScreenPreview() {
    val mockState = AiInvoiceUiState(
        isLoading = false,
        generatedInvoice = Invoice(
            id = "1",
            clientName = "John Doe",
            amount = 1500.0,
            service = "Web Development",
            status = InvoiceStatus.PENDING
        ),
        error = null
    )
    AiSmartInvoiceScreen(
        viewModel = null,
        onInvoiceSent = {},
        previewUiState = mockState
    )
}
