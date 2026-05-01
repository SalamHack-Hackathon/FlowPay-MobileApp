package com.abdallamusa.flowpay.presentation.clients

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.abdallamusa.flowpay.presentation.viewmodel.ClientsViewModel
import com.abdallamusa.flowpay.presentation.viewmodel.ClientsUiState
import com.abdallamusa.flowpay.presentation.viewmodel.ClientFilter
import com.abdallamusa.flowpay.presentation.viewmodel.ClientSummary
import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.InvoiceStatus
import com.abdallamusa.flowpay.presentation.components.FlowPayTopAppBar
import com.abdallamusa.flowpay.presentation.components.TopBarStyle
import com.abdallamusa.flowpay.ui.theme.BackgroundDark
import com.abdallamusa.flowpay.ui.theme.CardBackground
import com.abdallamusa.flowpay.ui.theme.ChatBubbleAI
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary
import com.abdallamusa.flowpay.ui.theme.SecondaryButtonBackground
import com.abdallamusa.flowpay.ui.theme.TextPrimary
import com.abdallamusa.flowpay.ui.theme.TextSecondary
import com.abdallamusa.flowpay.ui.theme.WarningColor
import com.abdallamusa.flowpay.utils.Strings
import java.util.Locale

@Composable
fun ClientsScreen(
    viewModel: ClientsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currency by viewModel.currency.collectAsState(initial = "ر.س")
    ClientsScreenContent(
        uiState = uiState,
        currency = currency,
        onMarkAsPaid = { viewModel.markAsPaid(it) },
        onFilterChange = { viewModel.setFilter(it) }
    )
}

@Composable
fun ClientsScreenContent(
    uiState: ClientsUiState,
    currency: String,
    onMarkAsPaid: (String) -> Unit,
    onFilterChange: (ClientFilter) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FilterButtons(
                clientSummaries = uiState.clientSummaries,
                selectedFilter = uiState.selectedFilter,
                onFilterChange = onFilterChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.filteredClients) { client ->
                    val clientInvoices = uiState.allInvoices.filter { it.clientName == client.clientName }
                    ClientCard(
                        client = client,
                        invoices = clientInvoices,
                        currency = currency,
                        onMarkAsPaid = onMarkAsPaid
                    )
                }
            }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = Strings.Clients.SEARCH_HINT,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = TextSecondary
                )
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions.Default,
            singleLine = true
        )

      
    }
}

@Composable
fun FilterButtons(
    clientSummaries: List<ClientSummary>,
    selectedFilter: ClientFilter,
    onFilterChange: (ClientFilter) -> Unit
) {
    val paidCount = clientSummaries.count { it.pendingCount == 0 && it.paidCount > 0 }
    val pendingCount = clientSummaries.count { it.pendingCount > 0 }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterButton(
            text = "مدفوع ($paidCount)",
            isSelected = selectedFilter == ClientFilter.PAID,
            onClick = { onFilterChange(ClientFilter.PAID) }
        )
        FilterButton(
            text = "بانتظار الدفع ($pendingCount)",
            isSelected = selectedFilter == ClientFilter.PENDING,
            onClick = { onFilterChange(ClientFilter.PENDING) }
        )
    }
}

@Composable
fun FilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) EmeraldPrimary else CardBackground
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(36.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) BackgroundDark else TextSecondary
        )
    }
}

@Composable
fun ClientCard(
    client: ClientSummary,
    invoices: List<Invoice>,
    currency: String,
    onMarkAsPaid: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(ChatBubbleAI),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = client.clientName.first().toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = client.clientName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${client.invoiceCount} فاتورة",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }

                ClientStatusBadge(
                    pendingCount = client.pendingCount,
                    paidCount = client.paidCount
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "الإجمالي",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "${String.format(Locale.US, "%,.0f", client.totalDue)} $currency",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                
                if (client.pendingCount > 0) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "مستحق",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "${String.format(Locale.US, "%,.0f", client.pendingAmount)} $currency",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarningColor
                        )
                    }
                } else {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "مدفوع",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "${String.format(Locale.US, "%,.0f", client.paidAmount)} $currency",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Show individual invoices with "تم الدفع" button for pending ones
            invoices.forEach { invoice ->
                InvoiceRow(
                    invoice = invoice,
                    currency = currency,
                    onMarkAsPaid = onMarkAsPaid
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun InvoiceRow(
    invoice: Invoice,
    currency: String,
    onMarkAsPaid: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (invoice.status == InvoiceStatus.PAID) 
                    EmeraldPrimary.copy(alpha = 0.1f) 
                else 
                    CardBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = invoice.service,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = "${String.format(Locale.US, "%,.0f", invoice.amount)} $currency",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        if (invoice.status == InvoiceStatus.PENDING) {
            Button(
                onClick = { onMarkAsPaid(invoice.id) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldPrimary
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = "تم الدفع",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = BackgroundDark
                )
            }
        } else {
            Text(
                text = "مدفوع",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = EmeraldPrimary,
                modifier = Modifier
                    .background(
                        color = EmeraldPrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun ClientStatusBadge(
    pendingCount: Int,
    paidCount: Int
) {
    val (text, color) = when {
        pendingCount > 0 -> Strings.Clients.PENDING to WarningColor
        paidCount > 0 -> Strings.Clients.PAID to EmeraldPrimary
        else -> Strings.Clients.PENDING to WarningColor
    }

    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = color,
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClientsScreenPreview() {
    val mockClients = listOf(
        ClientSummary(
            clientName = "شركة الأفق",
            totalDue = 15000.0,
            paidAmount = 5000.0,
            pendingAmount = 10000.0,
            invoiceCount = 3,
            paidCount = 1,
            pendingCount = 2
        ),
        ClientSummary(
            clientName = "مؤسسة النور",
            totalDue = 25000.0,
            paidAmount = 25000.0,
            pendingAmount = 0.0,
            invoiceCount = 2,
            paidCount = 2,
            pendingCount = 0
        )
    )
    
    val mockInvoices = listOf(
        Invoice(
            id = "1",
            clientName = "شركة الأفق",
            amount = 5000.0,
            service = "استشارات",
            status = InvoiceStatus.PAID
        ),
        Invoice(
            id = "2",
            clientName = "شركة الأفق",
            amount = 10000.0,
            service = "تصميم",
            status = InvoiceStatus.PENDING
        )
    )
    
    ClientsScreenContent(
        uiState = ClientsUiState(
            allInvoices = mockInvoices,
            clientSummaries = mockClients,
            filteredClients = mockClients,
            isLoading = false,
            error = null
        ),
        currency = "USD",
        onMarkAsPaid = {},
        onFilterChange = {}
    )
}
