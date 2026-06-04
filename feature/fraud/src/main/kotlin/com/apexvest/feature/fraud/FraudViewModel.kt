package com.apexvest.feature.fraud

import androidx.lifecycle.ViewModel
import com.apexvest.core.ai.BehavioralProfiler
import com.apexvest.core.ai.TouchEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FraudViewModel @Inject constructor(
    private val profiler: BehavioralProfiler
) : ViewModel() {

    fun onUserTouch(x: Float, y: Float, pressure: Float) {
        profiler.recordTouch(TouchEvent(x, y, pressure, System.currentTimeMillis()))
    }

    fun isInteractionGenuine(): Boolean {
        return profiler.analyzeIntegrity()
    }
}
