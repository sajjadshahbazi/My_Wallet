package org.sajad.shahabzi.navigation

internal class FakeAppNavigator : AppNavigator {

    data class NavigateCall(val route: String, val popUpTo: String?)

    val calls = mutableListOf<NavigateCall>()
    val navigatedRoutes: List<String> get() = calls.map { it.route }
    var backCount = 0

    override fun navigateTo(route: String, popUpTo: String?) {
        calls += NavigateCall(route, popUpTo)
    }

    override fun navigateBack() {
        backCount++
    }
}