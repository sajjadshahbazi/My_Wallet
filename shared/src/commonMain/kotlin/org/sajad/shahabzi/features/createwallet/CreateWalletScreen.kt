package org.sajad.shahabzi.features.createwallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.features.createwallet.components.DoneStep
import org.sajad.shahabzi.features.createwallet.components.PhraseStep
import org.sajad.shahabzi.features.createwallet.components.VerifyStep
import org.sajad.shahabzi.theme.AppDimens
import org.sajad.shahabzi.theme.AppTheme
import org.sajad.shahabzi.tools.secure.SecureScreen

@Composable
fun CreateWalletScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<CreateWalletViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SecureScreen {
        CreateWalletContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = modifier,
        )
    }
}

@Composable
private fun CreateWalletContent(
    state: CreateWalletState,
    onIntent: (CreateWalletIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(AppDimens.ScreenPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        when (state.step) {
            CreateWalletStep.Phrase -> PhraseStep(state = state, onIntent = onIntent)
            CreateWalletStep.Verify -> VerifyStep(state = state, onIntent = onIntent)
            CreateWalletStep.Done -> DoneStep(state = state, onIntent = onIntent)
        }
    }
}

// Sample data for previews only: not a valid mnemonic.
private val PreviewMnemonic =
    Mnemonic("alpha bravo charlie delta echo foxtrot golf hotel india juliet kilo lima")

@Preview
@Composable
private fun CreateWalletPhraseHiddenPreview() {
    AppTheme {
        CreateWalletContent(state = CreateWalletState(mnemonic = PreviewMnemonic), onIntent = {})
    }
}

@Preview
@Composable
private fun CreateWalletPhraseRevealedPreview() {
    AppTheme {
        CreateWalletContent(
            state = CreateWalletState(
                mnemonic = PreviewMnemonic,
                isRevealed = true,
                hasConfirmedBackup = true,
            ),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun CreateWalletGenerationErrorPreview() {
    AppTheme {
        CreateWalletContent(
            state = CreateWalletState(error = CreateWalletError.GenerationFailed),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun CreateWalletVerifyPreview() {
    AppTheme {
        CreateWalletContent(
            state = CreateWalletState(
                step = CreateWalletStep.Verify,
                mnemonic = PreviewMnemonic,
                challengePositions = listOf(3, 7, 11),
            ),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun CreateWalletVerifyErrorPreview() {
    AppTheme {
        CreateWalletContent(
            state = CreateWalletState(
                step = CreateWalletStep.Verify,
                mnemonic = PreviewMnemonic,
                challengePositions = listOf(3, 7, 11),
                error = CreateWalletError.VerificationFailed,
            ),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun CreateWalletDonePreview() {
    AppTheme {
        CreateWalletContent(
            state = CreateWalletState(step = CreateWalletStep.Done, walletAddress = "0xA1b2…c3D4"),
            onIntent = {},
        )
    }
}