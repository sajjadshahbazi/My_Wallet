package org.sajad.shahabzi.tools.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow

/** Collects one-time effects while the calling composable is in the composition. */
@Composable
fun <E> ObserveEffects(effects: Flow<E>, onEffect: suspend (E) -> Unit) {
    val currentOnEffect by rememberUpdatedState(onEffect)
    LaunchedEffect(effects) {
        effects.collect { currentOnEffect(it) }
    }
}