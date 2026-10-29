package org.sajad.shahabzi.features.recoverwallet

data class RecoverWalletState(
    val isSaving: Boolean = false,
    val error: RecoverWalletError? = null,
)