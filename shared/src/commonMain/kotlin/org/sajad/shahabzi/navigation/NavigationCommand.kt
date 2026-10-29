package org.sajad.shahabzi.navigation

internal sealed interface NavigationCommand {
    data class To(val route: String, val popUpTo: String?) : NavigationCommand
    data object Back : NavigationCommand
}