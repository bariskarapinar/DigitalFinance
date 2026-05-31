package com.apexvest.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apexvest.core.common.throttleForUi
import com.apexvest.core.network.MarketDataRepository
import com.apexvest.core.network.model.Ticker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    marketDataRepository: MarketDataRepository
) : ViewModel() {

    val tickerStream: StateFlow<List<Ticker>> = marketDataRepository
        .getMarketStream()
        .throttleForUi(100L) // Sample at 10Hz for UI
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
