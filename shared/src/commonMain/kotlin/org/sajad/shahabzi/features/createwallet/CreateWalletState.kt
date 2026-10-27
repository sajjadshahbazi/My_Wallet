package org.sajad.shahabzi.features.createwallet

import org.sajad.shahabzi.domain.wallet.model.Mnemonic

data class CreateWalletState(
    val mnemonic: Mnemonic? = null,
    val isRevealed: Boolean = false,
    val isGenerationFailed: Boolean = false,
)