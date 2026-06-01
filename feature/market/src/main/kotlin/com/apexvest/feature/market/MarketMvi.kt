package com.apexvest.feature.market

import com.apexvest.core.common.UiEffect
import com.apexvest.core.common.UiIntent
import com.apexvest.core.common.UiState
import com.apexvest.core.network.model.Ticker

data class MarketState(
    val tickers: List<Ticker> = emptyList(),
    val optimalAllocations: Map<String, Double> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

sealed class MarketIntent : UiIntent {
    data object LoadMarket : MarketIntent
    data class RebalancePortfolio(val riskProfile: String) : MarketIntent
    data class StakeTokens(val assetId: String, val amount: Double) : MarketIntent
}

sealed class MarketEffect : UiEffect {
    data class ShowToast(val message: String) : MarketEffect
    data class NavigateToDetails(val assetId: String) : MarketEffect
}
