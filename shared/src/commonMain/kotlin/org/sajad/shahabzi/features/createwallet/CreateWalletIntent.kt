package org.sajad.shahabzi.features.createwallet

sealed interface CreateWalletIntent {
    data object RevealToggled : CreateWalletIntent
    data class BackupConfirmedToggled(val confirmed: Boolean) : CreateWalletIntent
    data object ContinueClicked : CreateWalletIntent
    data object BackToPhraseClicked : CreateWalletIntent
    data class VerifyClicked(val answers: VerifyAnswers) : CreateWalletIntent
    data object RetryClicked : CreateWalletIntent
    data object DoneClicked : CreateWalletIntent
}