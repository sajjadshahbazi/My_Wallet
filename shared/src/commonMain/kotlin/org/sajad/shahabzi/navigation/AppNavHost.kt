package org.sajad.shahabzi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.sajad.shahabzi.common.NavigationKeys
import org.sajad.shahabzi.features.createwallet.CreateWalletScreen
import org.sajad.shahabzi.features.home.HomeScreen
import org.sajad.shahabzi.features.recoverwallet.RecoverWalletScreen
import org.sajad.shahabzi.features.startup.StartupScreen
import org.sajad.shahabzi.features.welcome.WelcomeScreen
import org.sajad.shahabzi.tools.mvi.ObserveEffects

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navigator = koinInject<ChannelAppNavigator>()

    ObserveEffects(navigator.commands) { command ->
        when (command) {
            is NavigationCommand.To -> {
                val root = if (command.clearBackStack) navController.rootRoute() else null
                navController.navigate(command.route) {
                    root?.let { popUpTo(it) { inclusive = true } }
                }
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
        composable(NavigationKeys.RECOVER_WALLET) { RecoverWalletScreen() }
        composable(NavigationKeys.HOME) { HomeScreen() }
    }
}

/** Route of the first real destination in the back stack (the one to pop up to in order to clear it). */
private fun NavController.rootRoute(): String? =
    currentBackStack.value.firstOrNull { it.destination !is NavGraph }?.destination?.route