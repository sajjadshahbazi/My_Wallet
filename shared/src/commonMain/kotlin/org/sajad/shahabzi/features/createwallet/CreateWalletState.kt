package org.sajad.shahabzi.features.createwallet

import androidx.compose.runtime.Immutable
import org.sajad.shahabzi.domain.wallet.model.Mnemonic

@Immutable
data class CreateWalletState(
    val step: CreateWalletStep = CreateWalletStep.Phrase,
    val mnemonic: Mnemonic? = null,
    val isRevealed: Boolean = false,
    val hasConfirmedBackup: Boolean = false,
    val challengePositions: List<Int> = emptyList(),
    val isSaving: Boolean = false,
    val walletAddress: String? = null,
    val error: CreateWalletError? = null,
)