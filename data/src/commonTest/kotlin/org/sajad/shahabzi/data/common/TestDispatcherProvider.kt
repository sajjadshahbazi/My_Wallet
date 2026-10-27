package org.sajad.shahabzi.data.common

import kotlinx.coroutines.CoroutineDispatcher
import org.sajad.shahabzi.domain.common.DispatcherProvider

internal class TestDispatcherProvider(dispatcher: CoroutineDispatcher) : DispatcherProvider {
    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
}