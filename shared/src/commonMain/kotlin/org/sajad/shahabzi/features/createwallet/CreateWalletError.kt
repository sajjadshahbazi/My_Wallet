package org.sajad.shahabzi.features.createwallet

sealed interface CreateWalletError {
    data object GenerationFailed : CreateWalletError
    data object VerificationFailed : CreateWalletError
    data object SaveFailed : CreateWalletError
    data object WalletAlreadyExists : CreateWalletError
}