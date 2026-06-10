package com.apexvest.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apexvest.core.designsystem.*
import com.apexvest.core.designsystem.components.AnimatedGradientText
import com.apexvest.core.designsystem.components.NeonCard
import com.apexvest.core.designsystem.components.PriceTicker
import com.apexvest.core.network.model.Ticker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val tickers by viewModel.tickerStream.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { AnimatedGradientText("ApexVest") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = DeepBlack
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepBlack, NeonPurple.copy(alpha = 0.1f), DeepBlack)
                    )
                )
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                TotalBalanceCard()
            }

            item {
                Text(
                    "Market Dynamics",
                    style = MaterialTheme.typography.titleLarge,
                    color = NeonCyan,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(
                items = tickers,
                key = { it.id },
                contentType = { it::class.java }
            ) { ticker ->
                VibrantTickerItem(ticker)
            }
        }
    }
}

@Composable
fun TotalBalanceCard() {
    NeonCard {
        Text("Total Portfolio Value", style = MaterialTheme.typography.labelMedium, color = NeonCyan)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "$1,248,592.42",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SuggestionChip(
                onClick = {},
                label = { Text("+2.4% Today", color = BullishGreen) },
                colors = SuggestionChipDefaults.suggestionChipColors(containerColor = GlassWhite)
            )
        }
    }
}

@Composable
fun VibrantTickerItem(ticker: Ticker) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = GlassWhite
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(ticker.symbol, style = MaterialTheme.typography.titleMedium, color = NeonCyan)
                Text(ticker.name, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            PriceTicker(price = ticker.price)
        }
    }
}
