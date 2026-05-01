package com.abdallamusa.flowpay.presentation.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import com.abdallamusa.flowpay.utils.Strings
import com.abdallamusa.flowpay.presentation.components.FlowPayTopAppBar
import com.abdallamusa.flowpay.presentation.components.TopBarStyle
import com.abdallamusa.flowpay.presentation.viewmodel.ReportsViewModel
import com.abdallamusa.flowpay.domain.model.Invoice
import com.abdallamusa.flowpay.domain.model.InvoiceStatus
import com.abdallamusa.flowpay.presentation.viewmodel.ReportsUiState
import com.abdallamusa.flowpay.ui.theme.BackgroundDark
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary
import com.abdallamusa.flowpay.ui.theme.TextPrimary
import com.abdallamusa.flowpay.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: ReportsViewModel,
    onReceiveClick: () -> Unit = {},
    onSendClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val currency by viewModel.currency.collectAsState(initial = "USD")
    DashboardScreenContent(
        uiState = uiState,
        currency = currency,
        onReceiveClick = onReceiveClick,
        onSendClick = onSendClick
    )
}

@Composable
fun DashboardScreenContent(
    uiState: ReportsUiState,
    currency: String,
    onReceiveClick: () -> Unit = {},
    onSendClick: () -> Unit = {}
) {
    Log.d("FlowPayDebug", "Dashboard recomposed. PendingInvoices: ${uiState.pendingInvoices.size}, Balance: ${uiState.totalNetProfit}")
    val totalBalance = uiState.totalNetProfit
    val monthlyIncome = uiState.incomeData.sum().toDouble()
    val monthlyExpenses = uiState.expenseData.sum().toDouble()
    val netProfit = uiState.totalNetProfit

    LazyColumn(
        modifier = Modifier
            .background(BackgroundDark)
            .padding(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            top = 24.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            BalanceCard(
                totalBalance = totalBalance,
                currency = currency,
                onReceiveClick = onReceiveClick,
                onSendClick = onSendClick
            )
        }
        
        item {
            FinancialSummaryCards(
                monthlyIncome = monthlyIncome,
                monthlyExpenses = monthlyExpenses,
                netProfit = netProfit,
                currency = currency
            )
        }
        
        item {
            CashFlowChart(
                incomeData = uiState.incomeData,
                expenseData = uiState.expenseData,
                months = uiState.months
            )
        }
        
        item {
            PendingInvoicesSection(invoices = uiState.pendingInvoices, currency = currency)
        }
    }
}



@Composable
fun BalanceCard(
    totalBalance: Double,
    currency: String,
    onReceiveClick: () -> Unit,
    onSendClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = Strings.Dashboard.TOTAL_BALANCE,
                fontSize = 14.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = currency,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${String.format(Locale.US, Strings.Common.CURRENCY_FORMAT, totalBalance)}!",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ReceiveButton(
                    onClick = onReceiveClick
                )
                SendButton(
                    onClick = onSendClick
                )
            }
        }
    }
}

@Composable
fun RowScope.ReceiveButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = EmeraldPrimary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = Strings.Dashboard.RECEIVE,
            tint = BackgroundDark,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = Strings.Dashboard.RECEIVE,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = BackgroundDark
        )
    }
}

@Composable
fun RowScope.SendButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowUpward,
            contentDescription = Strings.Dashboard.SEND,
            tint = EmeraldPrimary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = Strings.Dashboard.SEND,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = EmeraldPrimary
        )
    }
}

@Composable
fun FinancialSummaryCards(
    monthlyIncome: Double,
    monthlyExpenses: Double,
    netProfit: Double,
    currency: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            title = Strings.Dashboard.MONTHLY_INCOME,
            value = monthlyIncome,
            currency = currency,
            icon = Icons.Default.TrendingUp,
            iconColor = EmeraldPrimary
        )
        SummaryCard(
            title = Strings.Dashboard.MONTHLY_EXPENSES,
            value = monthlyExpenses,
            currency = currency,
            icon = Icons.Default.TrendingDown,
            iconColor = Color.Red
        )
        SummaryCard(
            title = Strings.Dashboard.NET_PROFIT,
            value = netProfit,
            currency = currency,
            icon = Icons.Default.AccountBalanceWallet,
            iconColor = EmeraldPrimary
        )
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: Double,
    currency: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val formattedValue = if (title == Strings.Dashboard.NET_PROFIT && value > 0) {
                        "+${String.format(Locale.US, Strings.Common.CURRENCY_FORMAT, value)}"
                    } else {
                        String.format(Locale.US, Strings.Common.CURRENCY_FORMAT, value)
                    }
                    Text(
                        text = "$currency $formattedValue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun CashFlowChart(
    incomeData: List<Float> = emptyList(),
    expenseData: List<Float> = emptyList(),
    months: List<String> = emptyList()
) {
    // Use provided data or default to zeros
    val chartData = incomeData.ifEmpty { List(6) { 0f } }
    val chartMonths = months.ifEmpty {
        listOf(
            Strings.Months.JANUARY, Strings.Months.FEBRUARY, Strings.Months.MARCH,
            Strings.Months.APRIL, Strings.Months.MAY, Strings.Months.JUNE
        )
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Strings.Dashboard.CASH_FLOW,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = Strings.Dashboard.LAST_SIX_MONTHS,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val barWidth = size.width / (chartData.size * 2f)
                val spacing = barWidth
                val maxBarHeight = size.height - 40.dp.toPx()
                
                chartData.forEachIndexed { index, value ->
                    val barHeight = (value / 100f) * maxBarHeight
                    val x = index * (barWidth + spacing) + spacing / 2
                    val y = size.height - barHeight - 40.dp.toPx()
                    
                    drawRoundRect(
                        color = EmeraldPrimary,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                    )
                    
                    val textPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 20f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    drawContext.canvas.nativeCanvas.drawText(
                        chartMonths[index],
                        x + barWidth / 2,
                        size.height - 10.dp.toPx(),
                        textPaint
                    )
                }
            }
        }
    }
}

@Composable
fun PendingInvoicesSection(invoices: List<Invoice>, currency: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = Strings.Dashboard.PENDING_BILLS,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                invoices.forEach { invoice ->
                    InvoiceItem(invoice = invoice, currency = currency)
                }
            }
        }
    }
}

@Composable
fun InvoiceItem(invoice: Invoice, currency: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = invoice.clientName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US).format(java.util.Date(invoice.date)),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Text(
                    text = "${String.format(Locale.US, Strings.Common.CURRENCY_FORMAT, invoice.amount)} $currency",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (invoice.status == InvoiceStatus.PENDING) {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Pending",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }
            } else if (invoice.status == InvoiceStatus.PAID) {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Paid",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = BackgroundDark
                    )
                }
            }
        }
    }
}

fun Modifier.glassCard(): Modifier {
    return this
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White.copy(alpha = 0.05f))
}


@Preview(showBackground = true)
@Composable
fun BalanceCardPreview() {
    BalanceCard(
        totalBalance = 0.0,
        currency = "USD",
        onReceiveClick = {},
        onSendClick = {}
    )
}


@Preview(showBackground = true)
@Composable
fun FinancialSummaryCardsPreview() {
    FinancialSummaryCards(
        monthlyIncome = 0.0,
        monthlyExpenses = 0.0,
        netProfit = 0.0,
        currency = "USD"
    )
}

@Preview(showBackground = true)
@Composable
fun SummaryCardPreview() {
    SummaryCard(
        title = Strings.Dashboard.MONTHLY_INCOME,
        value = 0.0,
        currency = "USD",
        icon = Icons.Default.TrendingUp,
        iconColor = EmeraldPrimary
    )
}

@Preview(showBackground = true)
@Composable
fun CashFlowChartPreview() {
    CashFlowChart()
}

@Preview(showBackground = true)
@Composable
fun InvoiceItemPreview() {
    InvoiceItem(
        invoice = Invoice(
            id = "1",
            clientName = "Sample Client",
            amount = 12500.0,
            service = "Web Development",
            date = System.currentTimeMillis(),
            status = InvoiceStatus.PENDING
        ),
        currency = "USD"
    )
}

// Full Dashboard Screen Preview with mock data
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreenContent(
        uiState = ReportsUiState(
            incomeData = listOf(50f, 75f, 60f, 90f, 80f, 95f),
            expenseData = listOf(30f, 45f, 35f, 50f, 40f, 55f),
            months = listOf(
                Strings.Months.JANUARY, Strings.Months.FEBRUARY, Strings.Months.MARCH,
                Strings.Months.APRIL, Strings.Months.MAY, Strings.Months.JUNE
            ),
            totalNetProfit = 45000.0,
            pendingInvoices = emptyList()
        ),
        currency = "USD",
        onReceiveClick = {},
        onSendClick = {}
    )
}
