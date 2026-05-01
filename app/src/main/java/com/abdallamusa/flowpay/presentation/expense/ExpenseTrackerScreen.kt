package com.abdallamusa.flowpay.presentation.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.abdallamusa.flowpay.presentation.viewmodel.ExpenseTrackerViewModel
import com.abdallamusa.flowpay.presentation.viewmodel.ExpenseTrackerUiState
import com.abdallamusa.flowpay.utils.Strings

import com.abdallamusa.flowpay.domain.model.ExpenseCategory
import com.abdallamusa.flowpay.domain.model.displayName
import com.abdallamusa.flowpay.presentation.components.CustomTextField
import com.abdallamusa.flowpay.presentation.components.FlowPayTopAppBar
import com.abdallamusa.flowpay.presentation.components.TopBarStyle
import com.abdallamusa.flowpay.ui.theme.BackgroundDark
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary
import com.abdallamusa.flowpay.ui.theme.TextPrimary
import com.abdallamusa.flowpay.ui.theme.TextSecondary
import com.abdallamusa.flowpay.ui.theme.glassCard
import com.abdallamusa.flowpay.utils.ValidationUtils.filterNumericInput


@Composable
fun ExpenseTrackerScreen(
    onExpenseAdded: () -> Unit
) {
    val viewModel: ExpenseTrackerViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isSuccess) {
        onExpenseAdded()
        viewModel.resetState()
    }

    ExpenseTrackerContent(
        uiState = uiState,
        onAddExpense = { name, amount, category ->
            viewModel.addExpense(name, amount, category)
        }
    )
}

@Composable
fun ExpenseTrackerContent(
    uiState: ExpenseTrackerUiState,
    onAddExpense: (String, Double, ExpenseCategory) -> Unit
) {
    var expenseName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ExpenseCategory?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FlowPayTopAppBar(style = TopBarStyle.LogoWithActions)
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
            value = expenseName,
            onValueChange = { expenseName = it },
            placeholder = Strings.Expense.EXPENSE_NAME_HINT,
            icon = Icons.Default.Description
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = amount,
            onValueChange = { amount = it.filterNumericInput() },
            placeholder = Strings.Expense.AMOUNT_HINT,
            icon = Icons.Default.AttachMoney,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .glassCard()
                .clickable { isDropdownExpanded = true },
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = Strings.Expense.CATEGORY,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCategory?.displayName() ?: Strings.Expense.CATEGORY_HINT,
                        fontSize = 16.sp,
                        color = if (selectedCategory == null) TextSecondary else TextPrimary
                    )
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(BackgroundDark),
            properties = PopupProperties(focusable = true)
        ) {
            ExpenseCategory.entries.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.displayName(),
                            color = TextPrimary
                        )
                    },
                    onClick = {
                        selectedCategory = category
                        isDropdownExpanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val amountValue = amount.toDoubleOrNull() ?: 0.0
                selectedCategory?.let { category ->
                    onAddExpense(expenseName, amountValue, category)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EmeraldPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !uiState.isLoading &&
                    expenseName.isNotBlank() &&
                    amount.isNotBlank() &&
                    amount.toDoubleOrNull() != null &&
                    amount.toDoubleOrNull()!! > 0 &&
                    selectedCategory != null
        ) {
            Text(
                text = Strings.Expense.ADD_EXPENSE,
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
fun ExpenseTrackerContentPreview() {
    ExpenseTrackerContent(
        uiState = ExpenseTrackerUiState(),
        onAddExpense = { _, _, _ -> }
    )
}
