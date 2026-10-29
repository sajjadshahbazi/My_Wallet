package org.sajad.shahabzi.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/** Replaces Dispatchers.Main so viewModelScope runs eagerly in tests. */
@OptIn(ExperimentalCoroutinesApi::class)
internal class MainDispatcherHelper {
    fun install() = Dispatchers.setMain(UnconfinedTestDispatcher())
    fun uninstall() = Dispatchers.resetMain()
}