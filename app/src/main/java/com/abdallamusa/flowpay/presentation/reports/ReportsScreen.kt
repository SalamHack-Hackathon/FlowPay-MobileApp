package com.abdallamusa.flowpay.presentation.reports

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.abdallamusa.flowpay.presentation.viewmodel.ReportsViewModel
import com.abdallamusa.flowpay.presentation.viewmodel.ReportsUiState
import com.abdallamusa.flowpay.presentation.viewmodel.ServiceCategory
import com.abdallamusa.flowpay.presentation.components.FlowPayTopAppBar
import com.abdallamusa.flowpay.presentation.components.TopBarStyle
import com.abdallamusa.flowpay.ui.theme.BackgroundDark
import com.abdallamusa.flowpay.ui.theme.CardBackground
import com.abdallamusa.flowpay.ui.theme.ChatBubbleAI
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary
import com.abdallamusa.flowpay.ui.theme.ErrorColor
import com.abdallamusa.flowpay.ui.theme.GlassBackground
import com.abdallamusa.flowpay.ui.theme.ProgressBackground
import com.abdallamusa.flowpay.ui.theme.TextPrimary
import com.abdallamusa.flowpay.ui.theme.TextSecondary
import com.abdallamusa.flowpay.utils.Strings
import java.util.Locale
import androidx.core.graphics.toColorInt

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ReportsScreenContent(uiState = uiState)
}

@Composable
fun ReportsScreenContent(
    uiState: ReportsUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            Text(
                text = Strings.Reports.SUBTITLE,
                fontSize = 14.sp,
                color = TextSecondary
            )

            TotalNetProfitCard(
                totalNetProfit = uiState.totalNetProfit,
                profitChange = uiState.profitChange
            )

            FinancialRatingCard(
                rating = uiState.financialRating
            )

            IncomeVsExpenseChart(
                incomeData = uiState.incomeData,
                expenseData = uiState.expenseData,
                months = uiState.months
            )

            TopServiceCategoriesCard(
                categories = uiState.topCategories
            )
    }
}

@Composable
fun TotalNetProfitCard(
    totalNetProfit: Double,
    profitChange: String
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
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = EmeraldPrimary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = Strings.Reports.CURRENT_MONTH,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = EmeraldPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = Strings.Reports.TOTAL_NET_PROFIT,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${String.format(Locale.US, "%,.0f", totalNetProfit)} رس",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$profitChange ${Strings.Reports.LAST_MONTH}",
                    fontSize = 14.sp,
                    color = if (profitChange.startsWith("+")) EmeraldPrimary else ErrorColor,
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun FinancialRatingCard(
    rating: Int
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
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = Strings.Reports.FINANCIAL_RATING,
                    fontSize = 16.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = Strings.Reports.EXCELLENT_PERFORMANCE,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.size(80.dp)
                ) {
                    val strokeWidth = 8.dp.toPx()
                    val radius = (size.minDimension - strokeWidth) / 2
                    val center = Offset(size.width / 2, size.height / 2)
                    
                    drawContext.canvas.nativeCanvas.apply {
                        drawCircle(
                            center.x,
                            center.y,
                            radius,
                            android.graphics.Paint().apply {
                                color = "#1E293B".toColorInt()
                                style = android.graphics.Paint.Style.STROKE
                                this.strokeWidth = strokeWidth
                            }
                        )
                        
                        val sweepAngle = (rating / 100f) * 360f
                        drawArc(
                            center.x - radius,
                            center.y - radius,
                            center.x + radius,
                            center.y + radius,
                            -90f,
                            sweepAngle,
                            false,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.parseColor("#10B981")
                                style = android.graphics.Paint.Style.STROKE
                                this.strokeWidth = strokeWidth
                                strokeCap = android.graphics.Paint.Cap.ROUND
                            }
                        )
                    }
                }
                
                Text(
                    text = "$rating%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            }
        }
    }
}

@Composable
fun IncomeVsExpenseChart(
    incomeData: List<Float>,
    expenseData: List<Float>,
    months: List<String>
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
                text = Strings.Reports.INCOME_VS_EXPENSE,
                fontSize = 18.sp,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = Strings.Reports.CASH_FLOW_ANALYSIS,
                fontSize = 14.sp,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary)
                    )
                    Text(
                        text = Strings.Reports.INCOME,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(ErrorColor)
                    )
                    Text(
                        text = Strings.Reports.EXPENSE,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val barWidth = size.width / (incomeData.size * 3f)
                val spacing = barWidth
                val maxBarHeight = size.height - 40.dp.toPx()
                val maxValue = maxOf(incomeData.maxOrNull() ?: 1f, expenseData.maxOrNull() ?: 1f)
                
                incomeData.forEachIndexed { index, value ->
                    val barHeight = (value / maxValue) * maxBarHeight
                    val x = index * (barWidth * 2 + spacing) + spacing
                    val y = size.height - barHeight - 30.dp.toPx()
                    
                    drawRoundRect(
                        color = EmeraldPrimary,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                }
                
                expenseData.forEachIndexed { index, value ->
                    val barHeight = (value / maxValue) * maxBarHeight
                    val x = index * (barWidth * 2 + spacing) + spacing + barWidth + 4.dp.toPx()
                    val y = size.height - barHeight - 30.dp.toPx()
                    
                    drawRoundRect(
                        color = ErrorColor,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                }
                
                val textPaint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 24f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                
                months.forEachIndexed { index, month ->
                    val x = index * (barWidth * 2 + spacing) + spacing + barWidth
                    drawContext.canvas.nativeCanvas.drawText(
                        month,
                        x,
                        size.height - 5.dp.toPx(),
                        textPaint
                    )
                }
            }
        }
    }
}

@Composable
fun TopServiceCategoriesCard(
    categories: List<ServiceCategory>
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
                text = Strings.Reports.TOP_SERVICE_CATEGORIES,
                fontSize = 18.sp,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            categories.forEach { category ->
                ServiceCategoryItem(category = category)
                if (category != categories.last()) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ServiceCategoryItem(category: ServiceCategory) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ChatBubbleAI),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = category.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }
            Text(
                text = "${String.format(Locale.US, "%,.0f", category.amount)} رس",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(ProgressBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(category.progress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(EmeraldPrimary)
            )
        }
    }
}

fun Modifier.glassCard(): Modifier {
    return this
        .clip(RoundedCornerShape(16.dp))
        .background(GlassBackground)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReportsScreenPreview() {
    ReportsScreenContent(
        uiState = ReportsUiState(
            totalNetProfit = 342500.0,
            profitChange = "+14.2%",
            financialRating = 49,
            incomeData = listOf(42000f, 38000f, 45000f, 52000f, 48000f, 55000f),
            expenseData = listOf(28000f, 32000f, 25000f, 35000f, 30000f, 28000f),
            months = listOf(
                Strings.ArabicMonths.OCTOBER,
                Strings.ArabicMonths.SEPTEMBER,
                Strings.ArabicMonths.AUGUST,
                Strings.ArabicMonths.JULY,
                Strings.ArabicMonths.JUNE,
                Strings.ArabicMonths.MAY
            ),
            topCategories = listOf(
                ServiceCategory(Strings.Reports.WEALTH_MANAGEMENT, 180000.0, 0.8f),
                ServiceCategory(Strings.Reports.TAX_CONSULTING, 95000.0, 0.5f),
                ServiceCategory(Strings.Reports.FINANCIAL_PLANNING, 72500.0, 0.35f)
            )
        )
    )
}
