package org.sajad.shahabzi.features.recoverwallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.sajad.shahabzi.resources.Res
import org.sajad.shahabzi.resources.recover_wallet_back
import org.sajad.shahabzi.resources.recover_wallet_button
import org.sajad.shahabzi.resources.recover_wallet_error_exists
import org.sajad.shahabzi.resources.recover_wallet_error_invalid_phrase
import org.sajad.shahabzi.resources.recover_wallet_error_save
import org.sajad.shahabzi.resources.recover_wallet_error_word_count
import org.sajad.shahabzi.resources.recover_wallet_label
import org.sajad.shahabzi.resources.recover_wallet_saving
import org.sajad.shahabzi.resources.recover_wallet_subtitle
import org.sajad.shahabzi.resources.recover_wallet_title
import org.sajad.shahabzi.theme.AppDimens
import org.sajad.shahabzi.theme.AppTheme
import org.sajad.shahabzi.tools.secure.SecureScreen

private const val PHRASE_FIELD_MIN_LINES = 3

@Composable
fun RecoverWalletScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<RecoverWalletViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SecureScreen {
        RecoverWalletContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = modifier,
        )
    }
}

@Composable
private fun RecoverWalletContent(
    state: RecoverWalletState,
    onIntent: (RecoverWalletIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // The typed phrase stays in local UI state only: never in the ViewModel state, never saved.
    var phrase by remember { mutableStateOf("") }

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
            text = stringResource(Res.string.recover_wallet_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(Res.string.recover_wallet_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedTextField(
            value = phrase,
            onValueChange = {
                phrase = it
                if (state.error != null) onIntent(RecoverWalletIntent.ErrorCleared)
            },
            label = { Text(stringResource(Res.string.recover_wallet_label)) },
            minLines = PHRASE_FIELD_MIN_LINES,
            enabled = !state.isSaving,
            isError = state.error != null,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        state.error?.let { error ->
            Text(
                text = stringResource(
                    when (error) {
                        RecoverWalletError.InvalidWordCount -> Res.string.recover_wallet_error_word_count
                        RecoverWalletError.InvalidPhrase -> Res.string.recover_wallet_error_invalid_phrase
                        RecoverWalletError.WalletAlreadyExists -> Res.string.recover_wallet_error_exists
                        RecoverWalletError.SaveFailed -> Res.string.recover_wallet_error_save
                    },
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
        Button(
            onClick = { onIntent(RecoverWalletIntent.RecoverClicked(RecoveryPhraseInput(phrase))) },
            enabled = phrase.isNotBlank() && !state.isSaving,
            modifier = Modifier.fillMaxWidth().height(AppDimens.ButtonHeight),
        ) {
            Text(
                stringResource(
                    if (state.isSaving) Res.string.recover_wallet_saving else Res.string.recover_wallet_button,
                ),
            )
        }
        OutlinedButton(
            onClick = { onIntent(RecoverWalletIntent.BackClicked) },
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth().height(AppDimens.ButtonHeight),
        ) {
            Text(stringResource(Res.string.recover_wallet_back))
        }
    }
}

@Preview
@Composable
private fun RecoverWalletContentPreview() {
    AppTheme { RecoverWalletContent(state = RecoverWalletState(), onIntent = {}) }
}

@Preview
@Composable
private fun RecoverWalletContentSavingPreview() {
    AppTheme { RecoverWalletContent(state = RecoverWalletState(isSaving = true), onIntent = {}) }
}

@Preview
@Composable
private fun RecoverWalletContentErrorPreview() {
    AppTheme {
        RecoverWalletContent(
            state = RecoverWalletState(error = RecoverWalletError.InvalidPhrase),
            onIntent = {},
        )
    }
}