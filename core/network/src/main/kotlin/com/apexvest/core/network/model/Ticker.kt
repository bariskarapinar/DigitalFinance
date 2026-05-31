package com.apexvest.core.network.model

import com.apexvest.core.common.StableModel
import kotlinx.serialization.Serializable

@Serializable
data class Ticker(
    val id: String,
    val symbol: String,
    val name: String,
    val price: Double,
    val change24h: Double,
    val history: List<Double> = emptyList()
) : StableModel
