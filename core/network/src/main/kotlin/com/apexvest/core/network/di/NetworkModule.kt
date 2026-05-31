package com.apexvest.core.network.di

import com.apexvest.core.network.MarketDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMarketDataRepository(): MarketDataRepository {
        return MarketDataRepository()
    }
}
