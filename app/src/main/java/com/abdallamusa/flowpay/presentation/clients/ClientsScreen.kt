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
    ClientsScreenContent(
        uiState = uiState,
        onMarkAsPaid = { viewModel.markAsPaid(it) },
        onFilterChange = { viewModel.setFilter(it) }
    )
}

@Composable
fun ClientsScreenContent(
    uiState: ClientsUiState,
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
                paidCount = uiState.paidCount,
                pendingCount = uiState.pendingCount,
                selectedFilter = uiState.selectedFilter,
                onFilterChange = onFilterChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.filteredInvoices) { invoice ->
                    InvoiceCard(
                        invoice = invoice,
                        onMarkAsPaid = { onMarkAsPaid(invoice.id) },
                        onDetailsClick = {}
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

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = BackgroundDark,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun FilterButtons(
    paidCount: Int,
    pendingCount: Int,
    selectedFilter: ClientFilter,
    onFilterChange: (ClientFilter) -> Unit
) {
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
fun InvoiceCard(
    invoice: Invoice,
    onMarkAsPaid: () -> Unit,
    onDetailsClick: () -> Unit
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
                        text = invoice.clientName.first().toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = invoice.clientName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US).format(java.util.Date(invoice.date)),
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }

                InvoiceStatusBadge(status = invoice.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (invoice.status == InvoiceStatus.PENDING) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = Strings.Clients.DUE_DATE,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US).format(java.util.Date(invoice.date)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = Strings.Clients.AMOUNT_DUE,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "${String.format(Locale.US, "%,.0f", invoice.amount)} SAR",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onMarkAsPaid,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = Strings.Clients.CONFIRM_PAYMENT,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = BackgroundDark
                        )
                    }
                    Button(
                        onClick = onDetailsClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryButtonBackground
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = Strings.Clients.DETAILS,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            } else if (invoice.status == InvoiceStatus.PAID) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Payment Date",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US).format(java.util.Date(invoice.date)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = Strings.Clients.AMOUNT_DUE,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "${String.format(Locale.US, "%,.0f", invoice.amount)} SAR",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryButtonBackground
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = Strings.Clients.COMPLETED,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                    Button(
                        onClick = onDetailsClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryButtonBackground
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = Strings.Clients.DETAILS,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InvoiceStatusBadge(status: InvoiceStatus) {
    val (text, color) = when (status) {
        InvoiceStatus.PAID -> Strings.Clients.PAID to EmeraldPrimary
        InvoiceStatus.PENDING -> Strings.Clients.PENDING to WarningColor
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
    val mockInvoices = listOf(
        Invoice(
            id = "1",
            clientName = "شركة الأفق",
            amount = 15000.0,
            service = "Web Development",
            date = System.currentTimeMillis(),
            status = InvoiceStatus.PENDING
        ),
        Invoice(
            id = "2",
            clientName = "مؤسسة النور",
            amount = 25000.0,
            service = "Mobile App",
            date = System.currentTimeMillis(),
            status = InvoiceStatus.PAID
        )
    )
    
    ClientsScreenContent(
        uiState = ClientsUiState(
            allInvoices = mockInvoices,
            filteredInvoices = mockInvoices,
            isLoading = false,
            error = null
        ),
        onMarkAsPaid = {},
        onFilterChange = {}
    )
}
