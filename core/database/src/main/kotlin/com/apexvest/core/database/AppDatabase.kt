package com.apexvest.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apexvest.core.database.dao.UserDao
import com.apexvest.core.database.dao.WalletDao
import com.apexvest.core.database.entity.UserEntity
import com.apexvest.core.database.entity.WalletEntity

@Database(entities = [UserEntity::class, WalletEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun walletDao(): WalletDao
}
