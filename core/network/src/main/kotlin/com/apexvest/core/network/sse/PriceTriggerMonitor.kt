package com.apexvest.core.network.sse

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

data class PriceAlert(
    val tickerSymbol: String,
    val targetPrice: Double,
    val currentPrice: Double
)

/**
 * ApexVest SSE Price Trigger Framework.
 * Passive unidirectional pipeline for price alerts.
 */
@Singleton
class PriceTriggerMonitor @Inject constructor() {

    /**
     * Simulates Server-Sent Events for market triggers.
     */
    fun listenToAlerts(): Flow<PriceAlert> = flow {
        while (true) {
            // Simulate random background alert strikes
            if (Math.random() > 0.95) {
                emit(PriceAlert("BTC", 100000.0, 100050.0))
            }
            delay(5000L)
        }
    }
}
