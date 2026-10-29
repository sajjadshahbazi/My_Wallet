package org.sajad.shahabzi.navigation

interface AppNavigator {
    /** Navigates to [route]. If [popUpTo] is set, that route and everything above it is removed first. */
    fun navigateTo(route: String, popUpTo: String? = null)
    fun navigateBack()
}