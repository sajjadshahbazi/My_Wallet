package org.sajad.shahabzi.features.createwallet

import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.sajad.shahabzi.common.NavigationKeys
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.usecase.CreateBackupChallengeUseCase
import org.sajad.shahabzi.domain.wallet.usecase.CreateWalletUseCase
import org.sajad.shahabzi.domain.wallet.usecase.GenerateMnemonicUseCase
import org.sajad.shahabzi.domain.wallet.usecase.VerifyBackupUseCase
import org.sajad.shahabzi.navigation.FakeAppNavigator
import org.sajad.shahabzi.testing.FakeAddressDeriver
import org.sajad.shahabzi.testing.FakeWalletRepository
import org.sajad.shahabzi.testing.MainDispatcherHelper

class CreateWalletViewModelTest {

    private val mainDispatcher = MainDispatcherHelper()
    private val generator = FakeMnemonicGenerator()
    private val repository = FakeWalletRepository()
    private val navigator = FakeAppNavigator()

    @BeforeTest
    fun setUp() {
        mainDispatcher.install()
    }

    @AfterTest
    fun tearDown() {
        mainDispatcher.uninstall()
    }

    private fun createViewModel() = CreateWalletViewModel(
        generateMnemonic = GenerateMnemonicUseCase(generator),
        createBackupChallenge = CreateBackupChallengeUseCase(Random(1)),
        verifyBackup = VerifyBackupUseCase(),
        createWallet = CreateWalletUseCase(FakeAddressDeriver(), repository),
        appNavigator = navigator,
    )

    private fun CreateWalletViewModel.reachVerifyStep() {
        onIntent(CreateWalletIntent.RevealToggled)
        onIntent(CreateWalletIntent.BackupConfirmedToggled(true))
        onIntent(CreateWalletIntent.ContinueClicked)
    }

    private fun CreateWalletViewModel.correctAnswers(): VerifyAnswers {
        val words = FakeMnemonicGenerator.PHRASE.split(' ')
        return VerifyAnswers(uiState.value.challengePositions.associateWith { words[it - 1] })
    }

    @Test
    fun init_whenGenerationSucceeds_showsHiddenPhraseOnPhraseStep() {
        val state = createViewModel().uiState.value

        assertEquals(CreateWalletStep.Phrase, state.step)
        assertNotNull(state.mnemonic)
        assertFalse(state.isRevealed)
        assertNull(state.error)
    }

    @Test
    fun init_whenGenerationFails_showsGenerationError() {
        generator.result = FakeMnemonicGenerator.FAILURE

        val state = createViewModel().uiState.value

        assertNull(state.mnemonic)
        assertEquals(CreateWalletError.GenerationFailed, state.error)
    }

    @Test
    fun onIntent_retryClicked_afterGenerationFailure_generatesAgain() {
        generator.result = FakeMnemonicGenerator.FAILURE
        val viewModel = createViewModel()
        generator.result = AppResult.Success(Mnemonic(FakeMnemonicGenerator.PHRASE))

        viewModel.onIntent(CreateWalletIntent.RetryClicked)

        assertEquals(2, generator.callCount)
        assertNotNull(viewModel.uiState.value.mnemonic)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun onIntent_continueClicked_withoutBackupConfirmed_staysOnPhraseStep() {
        val viewModel = createViewModel()

        viewModel.onIntent(CreateWalletIntent.ContinueClicked)

        assertEquals(CreateWalletStep.Phrase, viewModel.uiState.value.step)
    }

    @Test
    fun onIntent_continueClicked_afterConfirm_movesToVerifyWithThreePositions() {
        val viewModel = createViewModel()

        viewModel.reachVerifyStep()

        assertEquals(CreateWalletStep.Verify, viewModel.uiState.value.step)
        assertEquals(3, viewModel.uiState.value.challengePositions.size)
    }

    @Test
    fun onIntent_verifyClicked_withWrongWords_showsVerificationErrorAndDoesNotSave() {
        val viewModel = createViewModel()
        viewModel.reachVerifyStep()
        val wrong = VerifyAnswers(viewModel.uiState.value.challengePositions.associateWith { "wrong" })

        viewModel.onIntent(CreateWalletIntent.VerifyClicked(wrong))

        assertEquals(CreateWalletError.VerificationFailed, viewModel.uiState.value.error)
        assertTrue(repository.saved.isEmpty())
    }

    @Test
    fun onIntent_verifyClicked_withCorrectWords_savesWalletAndShowsDoneWithAddress() {
        val viewModel = createViewModel()
        viewModel.reachVerifyStep()

        viewModel.onIntent(CreateWalletIntent.VerifyClicked(viewModel.correctAnswers()))

        val state = viewModel.uiState.value
        assertEquals(CreateWalletStep.Done, state.step)
        assertEquals(FakeAddressDeriver.ADDRESS, state.walletAddress)
        assertNull(state.mnemonic)
        assertEquals(1, repository.saved.size)
    }

    @Test
    fun onIntent_verifyClicked_whenWalletAlreadyExists_showsAlreadyExistsError() {
        repository.saveResult = AppResult.Failure(AppError.WalletAlreadyExists)
        val viewModel = createViewModel()
        viewModel.reachVerifyStep()

        viewModel.onIntent(CreateWalletIntent.VerifyClicked(viewModel.correctAnswers()))

        assertEquals(CreateWalletError.WalletAlreadyExists, viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isSaving)
    }

    @Test
    fun onIntent_verifyClicked_whenSaveFails_showsSaveFailedAndKeepsPhrase() {
        repository.saveResult = AppResult.Failure(AppError.StorageFailed)
        val viewModel = createViewModel()
        viewModel.reachVerifyStep()

        viewModel.onIntent(CreateWalletIntent.VerifyClicked(viewModel.correctAnswers()))

        assertEquals(CreateWalletError.SaveFailed, viewModel.uiState.value.error)
        assertNotNull(viewModel.uiState.value.mnemonic)
    }

    @Test
    fun onIntent_backToPhraseClicked_returnsToPhraseAndClearsError() {
        val viewModel = createViewModel()
        viewModel.reachVerifyStep()
        viewModel.onIntent(
            CreateWalletIntent.VerifyClicked(VerifyAnswers(mapOf(1 to "wrong"))),
        )

        viewModel.onIntent(CreateWalletIntent.BackToPhraseClicked)

        assertEquals(CreateWalletStep.Phrase, viewModel.uiState.value.step)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun onIntent_doneClicked_navigatesHomeAndPopsWelcome() {
        val viewModel = createViewModel()

        viewModel.onIntent(CreateWalletIntent.DoneClicked)

        assertEquals(
            listOf(FakeAppNavigator.NavigateCall(NavigationKeys.HOME, clearBackStack = true)),
            navigator.calls,
        )
    }

    @Test
    fun state_toString_doesNotRevealPhrase() {
        val state = CreateWalletState(mnemonic = Mnemonic(FakeMnemonicGenerator.PHRASE))

        assertFalse(state.toString().contains("fake words"))
    }

    @Test
    fun verifyAnswers_toString_doesNotRevealWords() {
        assertFalse(VerifyAnswers(mapOf(1 to "secretword")).toString().contains("secretword"))
    }
}