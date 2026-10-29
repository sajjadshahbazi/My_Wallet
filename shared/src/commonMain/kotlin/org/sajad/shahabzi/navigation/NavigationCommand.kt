package org.sajad.shahabzi.navigation

internal sealed interface NavigationCommand {
    data class To(val route: String, val clearBackStack: Boolean) : NavigationCommand
    data object Back : NavigationCommand
}