package org.sajad.shahabzi.features.recoverwallet

sealed interface RecoverWalletError {
    data object InvalidWordCount : RecoverWalletError
    data object InvalidPhrase : RecoverWalletError
    data object WalletAlreadyExists : RecoverWalletError
    data object SaveFailed : RecoverWalletError
}