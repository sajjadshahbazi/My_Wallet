package org.sajad.shahabzi.navigation

interface AppNavigator {
    /** Navigates to [route]. With [clearBackStack] the whole back stack is removed first. */
    fun navigateTo(route: String, clearBackStack: Boolean = false)
    fun navigateBack()
}