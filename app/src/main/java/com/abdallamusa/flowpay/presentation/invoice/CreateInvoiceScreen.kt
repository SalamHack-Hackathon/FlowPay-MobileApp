package com.abdallamusa.flowpay.presentation.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.abdallamusa.flowpay.presentation.viewmodel.CreateInvoiceViewModel
import com.abdallamusa.flowpay.presentation.viewmodel.CreateInvoiceUiState
import com.abdallamusa.flowpay.utils.Strings
import com.abdallamusa.flowpay.presentation.components.CustomTextField
import com.abdallamusa.flowpay.presentation.components.FlowPayTopAppBar
import com.abdallamusa.flowpay.presentation.components.TopBarStyle
import com.abdallamusa.flowpay.ui.theme.BackgroundDark
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary

@Composable
fun CreateInvoiceScreen(
    onInvoiceCreated: () -> Unit
) {
    val viewModel: CreateInvoiceViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isSuccess) {
        onInvoiceCreated()
        viewModel.resetState()
    }

    CreateInvoiceContent(
        uiState = uiState,
        onCreateInvoice = { clientName, amount, service ->
            viewModel.createInvoice(clientName, amount, service)
        }
    )
}

@Composable
fun CreateInvoiceContent(
    uiState: CreateInvoiceUiState,
    onCreateInvoice: (String, Double, String) -> Unit
) {
    var clientName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var service by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FlowPayTopAppBar(style = TopBarStyle.Title(Strings.CreateInvoice.TITLE))
        }
    ) { paddingValues ->
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
            Spacer(modifier = Modifier.height(32.dp))

        CustomTextField(
            value = clientName,
            onValueChange = { clientName = it },
            placeholder = Strings.CreateInvoice.CLIENT_NAME_HINT,
            icon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = amount,
            onValueChange = { amount = it },
            placeholder = Strings.CreateInvoice.AMOUNT_HINT,
            icon = Icons.Default.AttachMoney,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = service,
            onValueChange = { service = it },
            placeholder = Strings.CreateInvoice.SERVICE_HINT,
            icon = Icons.Default.Work
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val amountValue = amount.toDoubleOrNull() ?: 0.0
                onCreateInvoice(clientName, amountValue, service)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EmeraldPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !uiState.isLoading
        ) {
            Text(
                text = Strings.CreateInvoice.CREATE,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BackgroundDark
            )
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

@Preview(showBackground = true)
@Composable
fun CreateInvoiceContentPreview() {
    CreateInvoiceContent(
        uiState = CreateInvoiceUiState(),
        onCreateInvoice = { _, _, _ -> }
    )
}
