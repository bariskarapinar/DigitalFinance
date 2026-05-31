package com.apexvest.feature.market

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apexvest.core.designsystem.components.StockChart
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    viewModel: MarketViewModel = hiltViewModel()
) {
    val asset by viewModel.selectedAsset.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Portfolio Analysis") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            asset?.let {
                Text(it.name, style = MaterialTheme.typography.headlineMedium)
                Text(it.symbol, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("Real-Time Candlestick Chart (Simulated)", style = MaterialTheme.typography.titleMedium)
                StockChart(
                    data = it.history,
                    isPositive = it.change24h > 0
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Order Book (Bids)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                        it.history.takeLast(5).forEach { price ->
                            Text("$${String.format(Locale.US, "%.2f", price * 0.999)}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                        Text("Order Book (Asks)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary)
                        it.history.takeLast(5).forEach { price ->
                            Text("$${String.format(Locale.US, "%.2f", price * 1.001)}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                        Text("BUY")
                    }
                    Button(onClick = { }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)) {
                        Text("SELL")
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
    }
}
