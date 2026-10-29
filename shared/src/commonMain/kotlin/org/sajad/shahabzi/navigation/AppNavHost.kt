package org.sajad.shahabzi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.sajad.shahabzi.common.NavigationKeys
import org.sajad.shahabzi.features.createwallet.CreateWalletScreen
import org.sajad.shahabzi.features.home.HomeScreen
import org.sajad.shahabzi.features.startup.StartupScreen
import org.sajad.shahabzi.features.welcome.WelcomeScreen
import org.sajad.shahabzi.tools.mvi.ObserveEffects

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navigator = koinInject<ChannelAppNavigator>()

    ObserveEffects(navigator.commands) { command ->
        when (command) {
            is NavigationCommand.To -> navController.navigate(command.route) {
                command.popUpTo?.let { popUpTo(it) { inclusive = true } }
            }
            NavigationCommand.Back -> navController.popBackStack()
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavigationKeys.STARTUP,
        modifier = modifier,
    ) {
        composable(NavigationKeys.STARTUP) { StartupScreen() }
        composable(NavigationKeys.WELCOME) { WelcomeScreen() }
        composable(NavigationKeys.CREATE_WALLET) { CreateWalletScreen() }
        composable(NavigationKeys.HOME) { HomeScreen() }
    }
}