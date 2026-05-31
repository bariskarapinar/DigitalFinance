package com.apexvest.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Base MVI ViewModel implementation for ApexVest.
 */
abstract class BaseMviViewModel<I : UiIntent, S : UiState, E : UiEffect>(
    initialState: S
) : ViewModel(), MviViewModel<I, S, E> {

    private val _uiState = MutableStateFlow(initialState)
    override val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<E>()
    override val uiEffect: SharedFlow<E> = _uiEffect.asSharedFlow()

    override fun onIntent(intent: I) {
        handleIntent(intent)
    }

    protected abstract fun handleIntent(intent: I)

    protected fun updateState(reducer: (S) -> S) {
        _uiState.update(reducer)
    }

    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _uiEffect.emit(effect)
        }
    }
}
