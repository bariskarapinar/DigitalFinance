package com.apexvest.core.common

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Base interface for all UI Intents (User Actions).
 */
interface UiIntent

/**
 * Base interface for all UI States (Immutable View State).
 */
interface UiState

/**
 * Base interface for all UI Effects (One-time Side Effects like navigation/toasts).
 */
interface UiEffect

/**
 * Base contract for ApexVest MVI ViewModels.
 */
interface MviViewModel<I : UiIntent, S : UiState, E : UiEffect> {
    val uiState: StateFlow<S>
    val uiEffect: SharedFlow<E>
    fun onIntent(intent: I)
}
