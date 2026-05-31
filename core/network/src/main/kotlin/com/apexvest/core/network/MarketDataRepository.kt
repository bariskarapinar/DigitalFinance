package com.apexvest.core.network

import com.apexvest.core.network.model.Ticker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MarketDataRepository @Inject constructor() {

    private val baseTickers = listOf(
        Ticker("1", "BTC", "Bitcoin", 95000.0, 2.5),
        Ticker("2", "ETH", "Ethereum", 2700.0, -1.2),
        Ticker("3", "SOL", "Solana", 145.0, 5.8),
        Ticker("4", "AAPL", "Apple Inc.", 220.0, 0.5),
        Ticker("5", "TSLA", "Tesla", 250.0, -3.4),
        Ticker("6", "NVDA", "Nvidia", 130.0, 1.2)
    )

    /**
     * ApexVest Enterprise Market Stream.
     * Uses callbackFlow to simulate WebSocket behavior with high-frequency noise.
     */
    @OptIn(kotlinx.coroutines.FlowPreview::class)
    fun streamMarketTicks(tickerSymbol: String? = null): Flow<List<Ticker>> = callbackFlow {
        var currentTickers = baseTickers.map { ticker ->
            ticker.copy(history = List(20) { ticker.price + Random.nextDouble(-5.0, 5.0) })
        }

        val job = launch {
            while (isActive) {
                currentTickers = currentTickers.map { ticker ->
                    val change = ticker.price * (Random.nextDouble(-0.001, 0.001))
                    val newPrice = ticker.price + change
                    ticker.copy(
                        price = newPrice,
                        history = (ticker.history.drop(1) + newPrice)
                    )
                }
                trySend(currentTickers)
                delay(50L) // 20Hz internal updates
            }
        }

        awaitClose { job.cancel() }
    }
    .flowOn(Dispatchers.Default)
    .conflate()
    .sample(100L) // ApexVest Performance Throttle

    // For compatibility with existing Dashboard code until fully migrated
    fun getMarketStream(): Flow<List<Ticker>> = streamMarketTicks()
}
