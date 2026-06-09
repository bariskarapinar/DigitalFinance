package com.apexvest

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apexvest.core.designsystem.DeepBlack
import com.apexvest.core.designsystem.NeonCyan
import com.apexvest.core.designsystem.NeonPurple
import com.apexvest.core.navigation.NavRoute
import com.apexvest.core.network.prefetch.HeuristicPrefetcher
import com.apexvest.feature.dashboard.DashboardScreen
import com.apexvest.feature.market.MarketScreen
import com.apexvest.feature.wallet.WalletScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class TopLevelDestination(
    val route: NavRoute,
    val icon: ImageVector,
    val label: String
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(NavRoute.Dashboard, Icons.Default.Home, "Nexus"),
    TopLevelDestination(NavRoute.Market, Icons.Default.List, "Market"),
    TopLevelDestination(NavRoute.Wallet, Icons.Default.Person, "Vault")
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // ApexVest Predictive Infrastructure
    val prefetchViewModel: PrefetchViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = DeepBlack,
                contentColor = NeonCyan
            ) {
                TOP_LEVEL_DESTINATIONS.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any { it.hasRoute(destination.route::class) } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (destination.route == NavRoute.Dashboard) {
                                prefetchViewModel.prefetcher.onNavigateToDashboard()
                            }
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = null) },
                        label = { Text(destination.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            selectedTextColor = NeonCyan,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = NeonPurple.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        },
        containerColor = DeepBlack
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Dashboard,
            modifier = Modifier.padding(padding)
        ) {
            composable<NavRoute.Dashboard> {
                DashboardScreen()
            }
            composable<NavRoute.Market> {
                MarketScreen()
            }
            composable<NavRoute.Wallet> {
                WalletScreen()
            }
        }
    }
}

/**
 * Simple ViewModel to hold the prefetcher for the MainScreen.
 */
@HiltViewModel
class PrefetchViewModel @Inject constructor(
    val prefetcher: HeuristicPrefetcher
) : androidx.lifecycle.ViewModel()
