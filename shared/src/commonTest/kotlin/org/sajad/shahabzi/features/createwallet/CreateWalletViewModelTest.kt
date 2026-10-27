package org.sajad.shahabzi.features.createwallet

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.usecase.GenerateMnemonicUseCase

class CreateWalletViewModelTest {

    private val generator = FakeMnemonicGenerator()

    private fun createViewModel() = CreateWalletViewModel(GenerateMnemonicUseCase(generator))

    @Test
    fun init_whenGenerationSucceeds_hasHiddenMnemonic() {
        val state = createViewModel().uiState.value

        assertNotNull(state.mnemonic)
        assertFalse(state.isRevealed)
        assertFalse(state.isGenerationFailed)
    }

    @Test
    fun init_whenGenerationFails_showsError() {
        generator.result = FakeMnemonicGenerator.FAILURE

        val state = createViewModel().uiState.value

        assertNull(state.mnemonic)
        assertTrue(state.isGenerationFailed)
    }

    @Test
    fun onIntent_revealToggled_togglesVisibility() {
        val viewModel = createViewModel()

        viewModel.onIntent(CreateWalletIntent.RevealToggled)
        assertTrue(viewModel.uiState.value.isRevealed)

        viewModel.onIntent(CreateWalletIntent.RevealToggled)
        assertFalse(viewModel.uiState.value.isRevealed)
    }

    @Test
    fun onIntent_retryClicked_afterFailure_generatesAgain() {
        generator.result = FakeMnemonicGenerator.FAILURE
        val viewModel = createViewModel()
        generator.result = AppResult.Success(Mnemonic(FakeMnemonicGenerator.PHRASE))

        viewModel.onIntent(CreateWalletIntent.RetryClicked)

        assertEquals(2, generator.callCount)
        assertNotNull(viewModel.uiState.value.mnemonic)
        assertFalse(viewModel.uiState.value.isGenerationFailed)
    }

    @Test
    fun state_toString_doesNotRevealPhrase() {
        val state = CreateWalletState(mnemonic = Mnemonic(FakeMnemonicGenerator.PHRASE))

        assertFalse(state.toString().contains("fake words"))
    }
}