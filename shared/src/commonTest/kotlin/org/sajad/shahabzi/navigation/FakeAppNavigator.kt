package org.sajad.shahabzi.navigation

internal class FakeAppNavigator : AppNavigator {

    data class NavigateCall(val route: String, val clearBackStack: Boolean)

    val calls = mutableListOf<NavigateCall>()
    val navigatedRoutes: List<String> get() = calls.map { it.route }
    var backCount = 0

    override fun navigateTo(route: String, clearBackStack: Boolean) {
        calls += NavigateCall(route, clearBackStack)
    }

    override fun navigateBack() {
        backCount++
    }
}