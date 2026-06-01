package com.apexvest.core.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

data class FxPair(
    val base: String,
    val target: String,
    val rate: Double,
    val spread: Double
)

/**
 * ApexVest Foreign Exchange (FX) Arbitrage Monitor.
 */
@Singleton
class FxRepository @Inject constructor() {

    private val baseRates = mapOf(
        "USDTRY" to 33.50,
        "EURUSD" to 1.08,
        "GBPUSD" to 1.26,
        "EURTRY" to 36.20
    )

    /**
     * Streams real-time FX pairs with dynamic spread calculations.
     */
    fun streamFxRates(): Flow<List<FxPair>> = flow {
        while (true) {
            val rates = baseRates.map { (pair, baseRate) ->
                val fluctuation = baseRate * Random.nextDouble(-0.0005, 0.0005)
                val currentRate = baseRate + fluctuation
                FxPair(
                    base = pair.take(3),
                    target = pair.takeLast(3),
                    rate = currentRate,
                    spread = currentRate * 0.001 // 0.1% dynamic spread
                )
            }
            emit(rates)
            delay(2000L) // 0.5Hz updates for FX
        }
    }
}
