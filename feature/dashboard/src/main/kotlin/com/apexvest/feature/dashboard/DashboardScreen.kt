package com.apexvest.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apexvest.core.designsystem.components.PriceTicker
import com.apexvest.core.network.model.Ticker
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val tickers by viewModel.tickerStream.collectAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Market Overview") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = tickers,
                key = { it.id }, // CRITICAL for performance: stable keys
                contentType = { it::class.java } // ApexVest optimization
            ) { ticker ->
                TickerItem(ticker)
            }
        }
    }
}

@Composable
fun TickerItem(ticker: Ticker) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(ticker.symbol, style = MaterialTheme.typography.titleMedium)
                Text(ticker.name, style = MaterialTheme.typography.bodySmall)
            }
            PriceTicker(price = ticker.price)
        }
    }
}
