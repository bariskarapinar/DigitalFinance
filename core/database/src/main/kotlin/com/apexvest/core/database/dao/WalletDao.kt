package com.apexvest.core.database.dao

import androidx.room.*
import com.apexvest.core.database.entity.WalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets WHERE userId = :userId")
    fun getWalletsForUser(userId: String): Flow<List<WalletEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWallet(wallet: WalletEntity)

    @Transaction
    suspend fun atomicSweep(
        userId: String,
        fromCurrency: String,
        toCurrency: String,
        amountFrom: Double,
        amountTo: Double,
        token: String
    ) {
        val fromWallet = getWalletSync(userId, fromCurrency)
        val toWallet = getWalletSync(userId, toCurrency)
        
        if (fromWallet != null && toWallet != null && fromWallet.balance >= amountFrom) {
            updateWallet(fromWallet.copy(
                balance = fromWallet.balance - amountFrom,
                lastRequestToken = token,
                syncStatus = "PENDING"
            ))
            updateWallet(toWallet.copy(
                balance = toWallet.balance + amountTo,
                lastRequestToken = token,
                syncStatus = "PENDING"
            ))
        }
    }

    @Query("SELECT * FROM wallets WHERE userId = :userId AND currencyCode = :currencyCode")
    suspend fun getWalletSync(userId: String, currencyCode: String): WalletEntity?
}
