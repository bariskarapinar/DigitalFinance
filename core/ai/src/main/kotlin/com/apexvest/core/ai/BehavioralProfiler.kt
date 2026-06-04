package com.apexvest.core.ai

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

data class TouchEvent(
    val x: Float,
    val y: Float,
    val pressure: Float,
    val timestamp: Long
)

/**
 * ApexVest Behavioral Telemetry Profiler.
 * Anonymously monitors interaction patterns for fraud detection.
 */
@Singleton
class BehavioralProfiler @Inject constructor() {

    private val touchBuffer = mutableListOf<TouchEvent>()
    private val MAX_BUFFER_SIZE = 100

    fun recordTouch(event: TouchEvent) {
        touchBuffer.add(event)
        if (touchBuffer.size > MAX_BUFFER_SIZE) {
            touchBuffer.removeAt(0)
        }
    }

    /**
     * Analyzes current interaction patterns against a generic "human" baseline.
     * Returns true if the behavior appears genuine.
     */
    fun analyzeIntegrity(): Boolean {
        if (touchBuffer.size < 10) return true // Not enough data
        
        // Calculate average pressure delta
        val pressureVariance = touchBuffer.map { it.pressure }.variance()
        
        Timber.d("BehavioralProfiler: Pressure Variance: $pressureVariance")
        
        // Robotic interaction (like a script) often has near-zero variance
        return pressureVariance > 0.0001
    }

    private fun List<Float>.variance(): Double {
        val avg = this.average()
        return this.map { Math.pow((it - avg).toDouble(), 2.0) }.average()
    }
}
