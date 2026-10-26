package org.sajad.shahabzi.navigation

internal class FakeAppNavigator : AppNavigator {
    val navigatedRoutes = mutableListOf<String>()
    var backCount = 0

    override fun navigateTo(route: String) {
        navigatedRoutes += route
    }

    override fun navigateBack() {
        backCount++
    }
}