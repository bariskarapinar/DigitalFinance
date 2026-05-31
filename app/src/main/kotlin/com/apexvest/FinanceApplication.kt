package com.apexvest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FinanceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // ApexVest Production Logging Configuration
        Timber.plant(Timber.DebugTree())
    }
}
