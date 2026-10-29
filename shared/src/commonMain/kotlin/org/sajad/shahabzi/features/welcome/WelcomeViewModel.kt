package org.sajad.shahabzi.features.welcome

import androidx.lifecycle.ViewModel
import org.sajad.shahabzi.common.NavigationKeys
import org.sajad.shahabzi.navigation.AppNavigator
import org.sajad.shahabzi.tools.mvi.IntentHandler
import org.sajad.shahabzi.tools.mvi.Mvi
import org.sajad.shahabzi.tools.mvi.mvi

class WelcomeViewModel(
    private val appNavigator: AppNavigator,
) : ViewModel(),
    Mvi<WelcomeState, Nothing> by mvi(WelcomeState()),
    IntentHandler<WelcomeIntent> {

    override fun onIntent(intent: WelcomeIntent) {
        when (intent) {
            is WelcomeIntent.TermsToggled -> updateUiState { copy(isTermsAccepted = intent.accepted) }
            WelcomeIntent.CreateWalletClicked -> navigateIfTermsAccepted(NavigationKeys.CREATE_WALLET)
            WelcomeIntent.RecoverWalletClicked -> navigateIfTermsAccepted(NavigationKeys.RECOVER_WALLET)
        }
    }

    private fun navigateIfTermsAccepted(route: String) {
        if (currentUiState.isTermsAccepted) {
            appNavigator.navigateTo(route)
        }
    }
}