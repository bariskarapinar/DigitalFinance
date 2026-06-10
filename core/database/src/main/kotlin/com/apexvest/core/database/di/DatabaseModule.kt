package com.apexvest.core.database.di

import android.content.Context
import androidx.room.Room
import com.apexvest.core.common.SecurityManager
import com.apexvest.core.database.AppDatabase
import com.apexvest.core.database.dao.UserDao
import com.apexvest.core.database.dao.WalletDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    init {
        System.loadLibrary("sqlcipher")
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        val passphrase = SecurityManager.getDatabasePassphrase()
        val factory = SupportOpenHelperFactory(passphrase)
        
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "finance_db"
        )
        .openHelperFactory(factory)
        .build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideWalletDao(db: AppDatabase): WalletDao = db.walletDao()
}
