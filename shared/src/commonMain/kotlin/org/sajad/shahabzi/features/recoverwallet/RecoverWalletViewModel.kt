package org.sajad.shahabzi.features.recoverwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sajad.shahabzi.common.NavigationKeys
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.usecase.RecoverWalletUseCase
import org.sajad.shahabzi.navigation.AppNavigator
import org.sajad.shahabzi.tools.mvi.IntentHandler
import org.sajad.shahabzi.tools.mvi.Mvi
import org.sajad.shahabzi.tools.mvi.mvi

class RecoverWalletViewModel(
    private val recoverWallet: RecoverWalletUseCase,
    private val appNavigator: AppNavigator,
) : ViewModel(),
    Mvi<RecoverWalletState, Nothing> by mvi(RecoverWalletState()),
    IntentHandler<RecoverWalletIntent> {

    override fun onIntent(intent: RecoverWalletIntent) {
        when (intent) {
            is RecoverWalletIntent.RecoverClicked -> recover(intent.input)
            RecoverWalletIntent.ErrorCleared -> updateUiState { copy(error = null) }
            RecoverWalletIntent.BackClicked -> appNavigator.navigateBack()
        }
    }

    private fun recover(input: RecoveryPhraseInput) {
        if (currentUiState.isSaving) return
        updateUiState { copy(isSaving = true, error = null) }
        viewModelScope.launch {
            when (val result = recoverWallet(input.phrase)) {
                is AppResult.Success -> {
                    updateUiState { copy(isSaving = false) }
                    appNavigator.navigateTo(NavigationKeys.HOME, clearBackStack = true)
                }
                is AppResult.Failure -> updateUiState {
                    copy(isSaving = false, error = result.error.toRecoverWalletError())
                }
            }
        }
    }

    private fun AppError.toRecoverWalletError(): RecoverWalletError = when (this) {
        AppError.InvalidWordCount -> RecoverWalletError.InvalidWordCount
        AppError.InvalidMnemonic -> RecoverWalletError.InvalidPhrase
        AppError.WalletAlreadyExists -> RecoverWalletError.WalletAlreadyExists
        else -> RecoverWalletError.SaveFailed
    }
}