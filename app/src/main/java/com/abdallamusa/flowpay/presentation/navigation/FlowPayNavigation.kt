package com.abdallamusa.flowpay.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abdallamusa.flowpay.presentation.aichat.AiChatScreen
import com.abdallamusa.flowpay.presentation.auth.AuthScreen
import com.abdallamusa.flowpay.presentation.clients.ClientsScreen
import com.abdallamusa.flowpay.presentation.components.FlowPayBottomBar
import com.abdallamusa.flowpay.presentation.components.FlowPayTopAppBar
import com.abdallamusa.flowpay.presentation.components.TopBarStyle
import com.abdallamusa.flowpay.presentation.dashboard.DashboardScreen
import com.abdallamusa.flowpay.presentation.expense.ExpenseTrackerScreen
import com.abdallamusa.flowpay.presentation.invoice.AiSmartInvoiceScreen
import com.abdallamusa.flowpay.presentation.invoice.CreateInvoiceScreen
import com.abdallamusa.flowpay.presentation.reports.ReportsScreen
import com.abdallamusa.flowpay.presentation.splash.SplashScreen
import com.abdallamusa.flowpay.presentation.viewmodel.ReportsViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.abdallamusa.flowpay.ui.theme.BackgroundDark
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary

sealed class Screen(val route: String) {
    // Global routes
    data object Splash : Screen("splash")
    data object Auth : Screen("auth")
    data object Main : Screen("main")

    // Bottom Nav Tabs
    data object Dashboard : Screen("dashboard")
    data object Clients : Screen("clients")
    data object Reports : Screen("reports")
    data object AiChat : Screen("ai_chat")

    // Sub-screens
    data object CreateInvoice : Screen("create_invoice")
    data object AiSmartInvoice : Screen("ai_smart_invoice")
    data object ExpenseTracker : Screen("expense_tracker")
}

@Composable
fun FlowPayNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            android.util.Log.d("FlowPayDebug", "NavGraph: Splash Screen composable created")
            SplashScreen(
                onNavigateToAuth = {
                    android.util.Log.d("FlowPayDebug", "NavGraph: Navigating Splash -> Auth")
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    android.util.Log.d("FlowPayDebug", "NavGraph: Navigating Splash -> Main")
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Auth Screen
        composable(Screen.Auth.route) {
            android.util.Log.d("FlowPayDebug", "NavGraph: Auth Screen composable created")
            AuthScreen(
                onLoginSuccess = {
                    android.util.Log.d("FlowPayDebug", "NavGraph: Navigating Auth -> Main")
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Screen (Host for bottom navigation)
        composable(Screen.Main.route) {
            android.util.Log.d("FlowPayDebug", "NavGraph: Main Screen composable created - THIS IS DASHBOARD")
            MainScreen(rootNavController = navController)
        }

        // Sub-screens (full screen, cover bottom nav)
        composable(Screen.CreateInvoice.route) {
            CreateInvoiceScreen(
                onInvoiceCreated = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AiSmartInvoice.route) {
            AiSmartInvoiceScreen(
                onInvoiceSent = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ExpenseTracker.route) {
            ExpenseTrackerScreen(
                onExpenseAdded = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val uiState by viewModel.uiState.collectAsState()

    // Get user name - use empty string for new users until data is loaded
    val userName = "" // Will be populated from user repository when available

    Scaffold(
        topBar = {
            when (currentDestination?.route) {
                Screen.Dashboard.route -> {
                    FlowPayTopAppBar(
                        style = TopBarStyle.Dashboard(
                            greeting = if (userName.isEmpty()) com.abdallamusa.flowpay.utils.Strings.Dashboard.WELCOME_USER else com.abdallamusa.flowpay.utils.Strings.Dashboard.GOOD_MORNING,
                            name = userName
                        )
                    )
                }
                Screen.Clients.route -> {
                    FlowPayTopAppBar(
                        style = TopBarStyle.Title(
                            title = "العملاء"
                        )
                    )
                }
                Screen.Reports.route -> {
                    FlowPayTopAppBar(
                        style = TopBarStyle.Title(
                            title = "التقارير"
                        )
                    )
                }
                Screen.AiChat.route -> {
                    FlowPayTopAppBar(
                        style = TopBarStyle.Title(
                            title = "المساعد"
                        )
                    )
                }
                else -> {}
            }
        },
        bottomBar = {
            FlowPayBottomBar(navController = bottomNavController)
        },
        floatingActionButton = {
            when (currentDestination?.route) {
                Screen.Dashboard.route -> {
                    FloatingActionButton(
                        onClick = { rootNavController.navigate(Screen.AiSmartInvoice.route) },
                        containerColor = EmeraldPrimary,
                        contentColor = BackgroundDark,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "AI Smart Invoice",
                            tint = BackgroundDark
                        )
                    }
                }
                Screen.Clients.route -> {
                    FloatingActionButton(
                        onClick = { rootNavController.navigate(Screen.CreateInvoice.route) },
                        containerColor = EmeraldPrimary,
                        contentColor = BackgroundDark,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Invoice",
                            tint = BackgroundDark
                        )
                    }
                }
                else -> {}
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = viewModel,
                    onReceiveClick = {
                        rootNavController.navigate(Screen.CreateInvoice.route)
                    },
                    onSendClick = {
                        rootNavController.navigate(Screen.ExpenseTracker.route)
                    }
                )
            }

            composable(Screen.Clients.route) {
                ClientsScreen()
            }

            composable(Screen.Reports.route) {
                ReportsScreen()
            }

            composable(Screen.AiChat.route) {
                AiChatScreen()
            }
        }
    }
}
