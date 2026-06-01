package com.apexvest.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey val currencyCode: String, // USD, EUR, GBP, TRY
    val balance: Double,
    val userId: String,
    val syncStatus: String = "SYNCED", // PENDING, SYNCED
    val lastRequestToken: String? = null
)
