package org.sajad.shahabzi.features.createwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sajad.shahabzi.common.NavigationKeys
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Wallet
import org.sajad.shahabzi.domain.wallet.usecase.CreateBackupChallengeUseCase
import org.sajad.shahabzi.domain.wallet.usecase.CreateWalletUseCase
import org.sajad.shahabzi.domain.wallet.usecase.GenerateMnemonicUseCase
import org.sajad.shahabzi.domain.wallet.usecase.VerifyBackupUseCase
import org.sajad.shahabzi.navigation.AppNavigator
import org.sajad.shahabzi.tools.mvi.IntentHandler
import org.sajad.shahabzi.tools.mvi.Mvi
import org.sajad.shahabzi.tools.mvi.mvi

class CreateWalletViewModel(
    private val generateMnemonic: GenerateMnemonicUseCase,
    private val createBackupChallenge: CreateBackupChallengeUseCase,
    private val verifyBackup: VerifyBackupUseCase,
    private val createWallet: CreateWalletUseCase,
    private val appNavigator: AppNavigator,
) : ViewModel(),
    Mvi<CreateWalletState, Nothing> by mvi(CreateWalletState()),
    IntentHandler<CreateWalletIntent> {

    init {
        generate()
    }

    override fun onIntent(intent: CreateWalletIntent) {
        when (intent) {
            CreateWalletIntent.RevealToggled -> updateUiState { copy(isRevealed = !isRevealed) }
            is CreateWalletIntent.BackupConfirmedToggled ->
                updateUiState { copy(hasConfirmedBackup = intent.confirmed) }
            CreateWalletIntent.ContinueClicked -> startVerification()
            CreateWalletIntent.BackToPhraseClicked ->
                updateUiState { copy(step = CreateWalletStep.Phrase, error = null) }
            is CreateWalletIntent.VerifyClicked -> verify(intent.answers)
            CreateWalletIntent.RetryClicked -> generate()
            CreateWalletIntent.DoneClicked ->
                appNavigator.navigateTo(NavigationKeys.HOME, clearBackStack  = true)
        }
    }

    private fun generate() {
        when (val result = generateMnemonic()) {
            is AppResult.Success -> updateUiState {
                copy(
                    step = CreateWalletStep.Phrase,
                    mnemonic = result.value,
                    isRevealed = false,
                    hasConfirmedBackup = false,
                    error = null,
                )
            }
            is AppResult.Failure -> updateUiState {
                copy(mnemonic = null, error = CreateWalletError.GenerationFailed)
            }
        }
    }

    private fun startVerification() {
        val state = currentUiState
        if (!state.hasConfirmedBackup || state.mnemonic == null) return
        val positions = createBackupChallenge()
        updateUiState {
            copy(step = CreateWalletStep.Verify, challengePositions = positions, error = null)
        }
    }

    private fun verify(answers: VerifyAnswers) {
        val state = currentUiState
        val mnemonic = state.mnemonic ?: return
        if (state.isSaving) return
        val answeredAllPositions = answers.words.keys == state.challengePositions.toSet()
        if (!answeredAllPositions || !verifyBackup(mnemonic, answers.words)) {
            updateUiState { copy(error = CreateWalletError.VerificationFailed) }
            return
        }
        updateUiState { copy(isSaving = true, error = null) }
        viewModelScope.launch {
            when (val result = createWallet(mnemonic)) {
                is AppResult.Success -> updateUiState {
                    copy(
                        step = CreateWalletStep.Done,
                        mnemonic = null,
                        isRevealed = false,
                        isSaving = false,
                        walletAddress = result.value.ethereumAddress(),
                    )
                }
                is AppResult.Failure -> updateUiState {
                    copy(isSaving = false, error = result.error.toCreateWalletError())
                }
            }
        }
    }

    private fun Wallet.ethereumAddress(): String? =
        addresses.firstOrNull { it.chain == Chain.ETHEREUM }?.address

    private fun AppError.toCreateWalletError(): CreateWalletError = when (this) {
        AppError.WalletAlreadyExists -> CreateWalletError.WalletAlreadyExists
        else -> CreateWalletError.SaveFailed
    }
}