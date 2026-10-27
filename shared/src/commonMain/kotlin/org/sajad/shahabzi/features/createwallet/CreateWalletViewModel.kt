package org.sajad.shahabzi.features.createwallet

import androidx.lifecycle.ViewModel
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.usecase.GenerateMnemonicUseCase
import org.sajad.shahabzi.tools.mvi.IntentHandler
import org.sajad.shahabzi.tools.mvi.Mvi
import org.sajad.shahabzi.tools.mvi.mvi

class CreateWalletViewModel(
    private val generateMnemonic: GenerateMnemonicUseCase,
) : ViewModel(),
    Mvi<CreateWalletState, Nothing> by mvi(CreateWalletState()),
    IntentHandler<CreateWalletIntent> {

    init {
        generate()
    }

    override fun onIntent(intent: CreateWalletIntent) {
        when (intent) {
            CreateWalletIntent.RevealToggled -> updateUiState { copy(isRevealed = !isRevealed) }
            CreateWalletIntent.RetryClicked -> generate()
        }
    }

    private fun generate() {
        when (val result = generateMnemonic()) {
            is AppResult.Success -> updateUiState {
                copy(mnemonic = result.value, isGenerationFailed = false)
            }
            is AppResult.Failure -> updateUiState {
                copy(mnemonic = null, isRevealed = false, isGenerationFailed = true)
            }
        }
    }
}