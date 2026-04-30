package com.abdallamusa.flowpay.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdallamusa.flowpay.utils.Strings
import com.abdallamusa.flowpay.ui.theme.BackgroundDark
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary
import com.abdallamusa.flowpay.ui.theme.TextPrimary
import com.abdallamusa.flowpay.ui.theme.TextSecondary

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Home : BottomNavItem(
        route = Strings.Routes.DASHBOARD,
        title = Strings.Navigation.HOME,
        icon = Icons.Default.Home
    )
    object Payments : BottomNavItem(
        route = "payments",
        title = Strings.Navigation.PAYMENTS,
        icon = Icons.Default.Payment
    )
    object Invoices : BottomNavItem(
        route = Strings.Routes.CREATE_INVOICE,
        title = Strings.Navigation.INVOICES,
        icon = Icons.Default.Receipt
    )
    object AI : BottomNavItem(
        route = Strings.Routes.AI_INVOICE,
        title = Strings.Navigation.AI,
        icon = Icons.Default.SmartToy
    )
    object Settings : BottomNavItem(
        route = "settings",
        title = Strings.Navigation.SETTINGS,
        icon = Icons.Default.Settings
    )
}

@Composable
fun FlowPayBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Payments,
        BottomNavItem.Invoices,
        BottomNavItem.AI,
        BottomNavItem.Settings
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = BackgroundDark
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                NavigationItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = { onNavigate(item.route) }
                )
            }
        }
    }
}

@Composable
fun NavigationItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(60.dp)
            .padding(vertical = 4.dp)
    ) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) EmeraldPrimary.copy(alpha = 0.2f) else Color.Transparent
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (isSelected) EmeraldPrimary else TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.title,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) EmeraldPrimary else TextSecondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlowPayBottomNavigationPreview() {
    FlowPayBottomNavigation(
        currentRoute = Strings.Routes.AI_INVOICE,
        onNavigate = {}
    )
}
