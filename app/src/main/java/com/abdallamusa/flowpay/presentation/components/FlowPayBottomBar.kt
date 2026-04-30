package com.abdallamusa.flowpay.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        route = "dashboard",
        title = "الرئيسية",
        icon = Icons.Default.Home
    )

    data object Clients : BottomNavItem(
        route = "clients",
        title = "العملاء",
        icon = Icons.Default.People
    )

    data object Reports : BottomNavItem(
        route = "reports",
        title = "التقارير",
        icon = Icons.Default.BarChart
    )

    data object Assistant : BottomNavItem(
        route = "ai_chat",
        title = "المساعد",
        icon = Icons.Default.SmartToy
    )

    companion object {
        val items = listOf(Home, Clients, Reports, Assistant)
    }
}

@Composable
fun FlowPayBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val backgroundColor = Color(0xFF111412).copy(alpha = 0.8f)
    val activeColor = Color(0xFF95d3ba)
    val inactiveColor = Color(0xFFbfc9c3)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .navigationBarsPadding()
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomNavItem.items.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo("dashboard") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = activeColor,
                        selectedTextColor = activeColor,
                        unselectedIconColor = inactiveColor,
                        unselectedTextColor = inactiveColor,
                        indicatorColor = activeColor.copy(alpha = 0.15f)
                    ),
                    alwaysShowLabel = true
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
fun FlowPayBottomBarPreview() {
    val navController = rememberNavController()

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(1f))
        FlowPayBottomBar(navController = navController)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412, name = "Bottom Bar - Reports Selected")
@Composable
fun FlowPayBottomBarReportsSelectedPreview() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(1f))

        val backgroundColor = Color(0xFF111412).copy(alpha = 0.8f)
        val activeColor = Color(0xFF95d3ba)
        val inactiveColor = Color(0xFFbfc9c3)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(bottom = 16.dp)
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                tonalElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                BottomNavItem.items.forEachIndexed { index, item ->
                    val isSelected = index == 2

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        },
                        selected = isSelected,
                        onClick = { },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = activeColor,
                            selectedTextColor = activeColor,
                            unselectedIconColor = inactiveColor,
                            unselectedTextColor = inactiveColor,
                            indicatorColor = activeColor.copy(alpha = 0.15f)
                        ),
                        alwaysShowLabel = true
                    )
                }
            }
        }
    }
}
