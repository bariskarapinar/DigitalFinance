package com.apexvest

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apexvest.core.navigation.NavRoute
import com.apexvest.feature.dashboard.DashboardScreen
import com.apexvest.feature.market.MarketScreen
import com.apexvest.feature.wallet.WalletScreen

data class TopLevelDestination(
    val route: NavRoute,
    val icon: ImageVector,
    val label: String
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(NavRoute.Dashboard, Icons.Default.Home, "Dashboard"),
    TopLevelDestination(NavRoute.Investment, Icons.Default.List, "Portfolio"),
    TopLevelDestination(NavRoute.Wallet, Icons.Default.Person, "Wallet")
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                TOP_LEVEL_DESTINATIONS.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any { it.hasRoute(destination.route::class) } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = null) },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Dashboard,
            modifier = Modifier.padding(padding)
        ) {
            composable<NavRoute.Dashboard> {
                DashboardScreen()
            }
            composable<NavRoute.Investment> {
                MarketScreen()
            }
            composable<NavRoute.Wallet> {
                WalletScreen()
            }
        }
    }
}
