package org.sajad.shahabzi.features.createwallet

sealed interface CreateWalletIntent {
    data object RevealToggled : CreateWalletIntent
    data object RetryClicked : CreateWalletIntent
}