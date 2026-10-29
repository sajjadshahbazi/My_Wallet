package org.sajad.shahabzi.features.recoverwallet

sealed interface RecoverWalletIntent {
    data class RecoverClicked(val input: RecoveryPhraseInput) : RecoverWalletIntent
    data object ErrorCleared : RecoverWalletIntent
    data object BackClicked : RecoverWalletIntent
}