package com.apexvest.core.common

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.sample

/**
 * Marks a class as [Immutable] for the Compose compiler.
 * This is useful for wrapping external models or data classes that might not be 
 * automatically inferred as immutable.
 */
@Immutable
data class UiState<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Marks a class as [Stable] for the Compose compiler.
 */
@Stable
interface StableModel

/**
 * Flow operator for high-frequency data.
 * Conflates the flow and samples it at the given interval to prevent UI jank.
 */
fun <T> Flow<T>.throttleForUi(intervalMs: Long = 100L): Flow<T> {
    return this.conflate().sample(intervalMs)
}
