package org.sajad.shahabzi.features.welcome

sealed interface WelcomeIntent {
    data class TermsToggled(val accepted: Boolean) : WelcomeIntent
    data object CreateWalletClicked : WelcomeIntent
    data object ImportWalletClicked : WelcomeIntent
}