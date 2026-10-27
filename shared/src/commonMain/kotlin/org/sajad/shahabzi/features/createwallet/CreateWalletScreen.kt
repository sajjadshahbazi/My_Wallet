package org.sajad.shahabzi.features.createwallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.sajad.shahabzi.common.AppConstants
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength
import org.sajad.shahabzi.resources.Res
import org.sajad.shahabzi.resources.create_wallet_error
import org.sajad.shahabzi.resources.create_wallet_hide
import org.sajad.shahabzi.resources.create_wallet_masked_word
import org.sajad.shahabzi.resources.create_wallet_retry
import org.sajad.shahabzi.resources.create_wallet_reveal
import org.sajad.shahabzi.resources.create_wallet_title
import org.sajad.shahabzi.resources.create_wallet_warning
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
    val words = remember(state.mnemonic, state.isRevealed) {
        if (state.isRevealed) state.mnemonic?.reveal()?.split(' ') else null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(AppDimens.ScreenPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing),
    ) {
        Text(
            text = stringResource(Res.string.create_wallet_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        RecoveryWarning()
        if (state.isGenerationFailed) {
            GenerationError(onRetry = { onIntent(CreateWalletIntent.RetryClicked) })
        } else {
            PhraseGrid(words = words)
            OutlinedButton(
                onClick = { onIntent(CreateWalletIntent.RevealToggled) },
                enabled = state.mnemonic != null,
                modifier = Modifier.fillMaxWidth().height(AppDimens.ButtonHeight),
            ) {
                Text(
                    stringResource(
                        if (state.isRevealed) Res.string.create_wallet_hide else Res.string.create_wallet_reveal,
                    ),
                )
            }
        }
    }
}

@Composable
private fun RecoveryWarning(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.CardCorner),
        color = MaterialTheme.colorScheme.errorContainer,
    ) {
        Text(
            text = stringResource(Res.string.create_wallet_warning),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(AppDimens.CardPadding),
        )
    }
}

@Composable
private fun GenerationError(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.ItemSpacing),
    ) {
        Text(
            text = stringResource(Res.string.create_wallet_error),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth().height(AppDimens.ButtonHeight),
        ) {
            Text(stringResource(Res.string.create_wallet_retry))
        }
    }
}

@Composable
private fun PhraseGrid(words: List<String>?, modifier: Modifier = Modifier) {
    val wordCount = MnemonicLength.WORDS_12.wordCount
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing),
    ) {
        (1..wordCount).chunked(AppConstants.PHRASE_GRID_COLUMNS).forEach { rowNumbers ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing),
            ) {
                rowNumbers.forEach { number ->
                    PhraseChip(
                        number = number,
                        word = words?.getOrNull(number - 1),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun PhraseChip(number: Int, word: String?, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(AppDimens.ChipHeight),
        shape = RoundedCornerShape(AppDimens.ChipCorner),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(AppDimens.BorderWidth, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = AppDimens.ChipPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing),
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(AppDimens.ChipNumberWidth),
            )
            Text(
                text = word ?: stringResource(Res.string.create_wallet_masked_word),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// Sample data for previews only: not a valid mnemonic.
private val PreviewMnemonic =
    Mnemonic("alpha bravo charlie delta echo foxtrot golf hotel india juliet kilo lima")

@Preview
@Composable
private fun CreateWalletContentHiddenPreview() {
    AppTheme {
        CreateWalletContent(state = CreateWalletState(mnemonic = PreviewMnemonic), onIntent = {})
    }
}

@Preview
@Composable
private fun CreateWalletContentRevealedPreview() {
    AppTheme {
        CreateWalletContent(
            state = CreateWalletState(mnemonic = PreviewMnemonic, isRevealed = true),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun CreateWalletContentErrorPreview() {
    AppTheme {
        CreateWalletContent(state = CreateWalletState(isGenerationFailed = true), onIntent = {})
    }
}