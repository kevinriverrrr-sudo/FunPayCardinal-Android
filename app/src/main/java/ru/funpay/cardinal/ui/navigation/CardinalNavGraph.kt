package ru.funpay.cardinal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.funpay.cardinal.data.db.CardinalDatabase
import ru.funpay.cardinal.viewmodel.*
import ru.funpay.cardinal.ui.screens.*

sealed class Screen(val route: String) {
    data object Setup : Screen("setup")
    data object Dashboard : Screen("dashboard")
    data object Settings : Screen("settings")
    data object Logs : Screen("logs")
    data object AutoDelivery : Screen("auto_delivery")
    data object AutoResponse : Screen("auto_response")
    data object TelegramSettings : Screen("telegram_settings")
    data object ProxySettings : Screen("proxy_settings")
}

@Composable
fun CardinalNavGraph(database: CardinalDatabase) {
    val navController = rememberNavController()
    val repository = ru.funpay.cardinal.data.repository.ConfigRepository(database.configDao())
    val setupViewModel = SetupViewModel(repository)
    val isConfigured = setupViewModel.isConfigured.collectAsStateWithLifecycle(initialValue = false)

    LaunchedEffect(isConfigured.value) {
        if (isConfigured.value && navController.currentDestination?.route == Screen.Setup.route) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Setup.route) { inclusive = true }
            }
        }
    }

    val startDestination = if (isConfigured.value) Screen.Dashboard.route else Screen.Setup.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Setup.route) {
            SetupScreen(
                viewModel = setupViewModel,
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Setup.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            val dashboardViewModel = DashboardViewModel(repository)
            DashboardScreen(
                viewModel = dashboardViewModel,
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToLogs = { navController.navigate(Screen.Logs.route) },
                onNavigateToSetup = { navController.navigate(Screen.Setup.route) }
            )
        }
        composable(Screen.Settings.route) {
            val settingsViewModel = SettingsViewModel(repository)
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToAutoDelivery = { navController.navigate(Screen.AutoDelivery.route) },
                onNavigateToAutoResponse = { navController.navigate(Screen.AutoResponse.route) },
                onNavigateToTelegram = { navController.navigate(Screen.TelegramSettings.route) },
                onNavigateToProxy = { navController.navigate(Screen.ProxySettings.route) }
            )
        }
        composable(Screen.Logs.route) {
            LogsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.AutoDelivery.route) {
            AutoDeliveryScreen(
                viewModel = SettingsViewModel(repository),
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AutoResponse.route) {
            AutoResponseScreen(
                viewModel = SettingsViewModel(repository),
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.TelegramSettings.route) {
            TelegramSettingsScreen(
                viewModel = SetupViewModel(repository),
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ProxySettings.route) {
            ProxySettingsScreen(
                viewModel = SettingsViewModel(repository),
                onBack = { navController.popBackStack() }
            )
        }
    }
}
