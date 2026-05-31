package com.apexvest.feature.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apexvest.core.common.throttleForUi
import com.apexvest.core.network.MarketDataRepository
import com.apexvest.core.network.model.Ticker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    marketDataRepository: MarketDataRepository
) : ViewModel() {

    // For simplicity, we just take the first ticker as "selected"
    val selectedAsset: StateFlow<Ticker?> = marketDataRepository
        .getMarketStream()
        .map { it.firstOrNull() }
        .throttleForUi(200L)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
