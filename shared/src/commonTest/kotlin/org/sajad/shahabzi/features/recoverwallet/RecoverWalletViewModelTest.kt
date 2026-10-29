package org.sajad.shahabzi.features.recoverwallet

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import org.sajad.shahabzi.common.NavigationKeys
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.usecase.CreateWalletUseCase
import org.sajad.shahabzi.domain.wallet.usecase.RecoverWalletUseCase
import org.sajad.shahabzi.navigation.FakeAppNavigator
import org.sajad.shahabzi.testing.FakeAddressDeriver
import org.sajad.shahabzi.testing.FakeWalletRepository
import org.sajad.shahabzi.testing.MainDispatcherHelper

class RecoverWalletViewModelTest {

    private val mainDispatcher = MainDispatcherHelper()
    private val deriver = FakeAddressDeriver()
    private val repository = FakeWalletRepository()
    private val navigator = FakeAppNavigator()

    private val validPhrase = "alpha bravo charlie delta echo foxtrot golf hotel india juliet kilo lima"

    @BeforeTest
    fun setUp() {
        mainDispatcher.install()
    }

    @AfterTest
    fun tearDown() {
        mainDispatcher.uninstall()
    }

    private fun createViewModel() = RecoverWalletViewModel(
        recoverWallet = RecoverWalletUseCase(CreateWalletUseCase(deriver, repository)),
        appNavigator = navigator,
    )

    private fun RecoverWalletViewModel.recover(phrase: String) =
        onIntent(RecoverWalletIntent.RecoverClicked(RecoveryPhraseInput(phrase)))

    @Test
    fun onIntent_recoverClicked_withValidPhrase_navigatesHomeClearingBackStack() {
        val viewModel = createViewModel()

        viewModel.recover(validPhrase)

        assertEquals(
            listOf(FakeAppNavigator.NavigateCall(NavigationKeys.HOME, true)),
            navigator.calls,
        )
        assertEquals(1, repository.saved.size)
        assertFalse(viewModel.uiState.value.isSaving)
    }

    @Test
    fun onIntent_recoverClicked_withWrongWordCount_showsWordCountError() {
        val viewModel = createViewModel()

        viewModel.recover("alpha bravo")

        assertEquals(RecoverWalletError.InvalidWordCount, viewModel.uiState.value.error)
        assertEquals(emptyList(), navigator.calls)
    }

    @Test
    fun onIntent_recoverClicked_withInvalidPhrase_showsInvalidPhraseError() {
        deriver.result = AppResult.Failure(AppError.InvalidMnemonic)
        val viewModel = createViewModel()

        viewModel.recover(validPhrase)

        assertEquals(RecoverWalletError.InvalidPhrase, viewModel.uiState.value.error)
    }

    @Test
    fun onIntent_recoverClicked_whenWalletExists_showsAlreadyExistsError() {
        repository.saveResult = AppResult.Failure(AppError.WalletAlreadyExists)
        val viewModel = createViewModel()

        viewModel.recover(validPhrase)

        assertEquals(RecoverWalletError.WalletAlreadyExists, viewModel.uiState.value.error)
    }

    @Test
    fun onIntent_recoverClicked_whenSaveFails_showsSaveFailedError() {
        repository.saveResult = AppResult.Failure(AppError.StorageFailed)
        val viewModel = createViewModel()

        viewModel.recover(validPhrase)

        assertEquals(RecoverWalletError.SaveFailed, viewModel.uiState.value.error)
    }

    @Test
    fun onIntent_errorCleared_removesError() {
        val viewModel = createViewModel()
        viewModel.recover("alpha")

        viewModel.onIntent(RecoverWalletIntent.ErrorCleared)

        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun onIntent_backClicked_navigatesBack() {
        val viewModel = createViewModel()

        viewModel.onIntent(RecoverWalletIntent.BackClicked)

        assertEquals(1, navigator.backCount)
    }

    @Test
    fun recoveryPhraseInput_toString_doesNotRevealPhrase() {
        assertFalse(RecoveryPhraseInput("secret words here").toString().contains("secret"))
    }
}