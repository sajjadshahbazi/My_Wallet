package org.sajad.shahabzi.tools.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/** State and one-time effects of a screen. Use `Nothing` as [E] when the screen has no effects. */
interface Mvi<S, E> {
    val uiState: StateFlow<S>
    val effects: Flow<E>
    val currentUiState: S
    fun updateUiState(reducer: S.() -> S)
    fun sendEffect(effect: E)
}

/** Receives the intents sent by the UI. */
interface IntentHandler<I> {
    fun onIntent(intent: I)
}

fun <S, E> mvi(initialState: S): Mvi<S, E> = MviDelegate(initialState)