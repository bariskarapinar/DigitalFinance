package com.apexvest.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute {
    @Serializable
    data object Dashboard : NavRoute

    @Serializable
    data object Investment : NavRoute

    @Serializable
    data object Wallet : NavRoute

    @Serializable
    data class AssetDetails(val assetId: String, val assetName: String) : NavRoute
}
