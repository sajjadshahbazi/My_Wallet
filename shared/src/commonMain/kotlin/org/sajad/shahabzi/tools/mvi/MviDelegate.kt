package org.sajad.shahabzi.tools.mvi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

internal class MviDelegate<S, E>(initialState: S) : Mvi<S, E> {

    private val state = MutableStateFlow(initialState)
    private val effectChannel = Channel<E>(Channel.UNLIMITED)

    override val uiState: StateFlow<S> = state.asStateFlow()
    override val effects: Flow<E> = effectChannel.receiveAsFlow()
    override val currentUiState: S get() = state.value

    override fun updateUiState(reducer: S.() -> S) {
        state.update { it.reducer() }
    }

    override fun sendEffect(effect: E) {
        effectChannel.trySend(effect)
    }
}