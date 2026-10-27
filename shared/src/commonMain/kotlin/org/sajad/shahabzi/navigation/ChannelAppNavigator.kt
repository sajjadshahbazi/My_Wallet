package org.sajad.shahabzi.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/** Emits navigation commands; AppNavHost collects them and applies them to the NavController. */
internal class ChannelAppNavigator : AppNavigator {

    private val channel = Channel<NavigationCommand>(Channel.UNLIMITED)

    val commands: Flow<NavigationCommand> = channel.receiveAsFlow()

    override fun navigateTo(route: String) {
        channel.trySend(NavigationCommand.To(route))
    }

    override fun navigateBack() {
        channel.trySend(NavigationCommand.Back)
    }
}