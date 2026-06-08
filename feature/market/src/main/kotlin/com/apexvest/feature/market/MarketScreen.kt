package com.apexvest.feature.market

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apexvest.core.designsystem.*
import com.apexvest.core.designsystem.components.CandleData
import com.apexvest.core.designsystem.components.CandlestickChart
import com.apexvest.core.designsystem.components.NeonCard
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    viewModel: MarketViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    
    // Simulate some candle data
    val candleData = remember {
        List(30) { i ->
            CandleData(
                open = 100f + (Math.random() * 20).toFloat(),
                close = 100f + (Math.random() * 20).toFloat(),
                high = 130f,
                low = 90f,
                timestamp = System.currentTimeMillis() - i * 60000
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Institutional Trading", color = NeonCyan) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = DeepBlack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            NeonCard(modifier = Modifier.height(300.dp)) {
                Text("AI-OPTIMIZED CANDLESTICK ENGINE", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
                Spacer(modifier = Modifier.height(16.dp))
                CandlestickChart(data = candleData)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onIntent(MarketIntent.RebalancePortfolio("Aggressive")) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
            ) {
                Text("Robo-Advisory: Auto-Rebalance Portfolio")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.optimalAllocations.isNotEmpty()) {
                Text("AI REBALANCING COMPLETE", color = BullishGreen, style = MaterialTheme.typography.labelMedium)
                state.optimalAllocations.forEach { (symbol, weight) ->
                    Text("$symbol: ${String.format(Locale.US, "%.1f", weight * 100)}%", color = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedButton(
                onClick = { viewModel.onIntent(MarketIntent.StakeTokens("ETH", 1.0)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Web3: Authorize DeFi Staking via HD Wallet", color = NeonPink)
            }
        }
    }
}
