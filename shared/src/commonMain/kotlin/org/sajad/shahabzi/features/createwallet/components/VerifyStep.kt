package org.sajad.shahabzi.features.createwallet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.stringResource
import org.sajad.shahabzi.features.createwallet.CreateWalletIntent
import org.sajad.shahabzi.features.createwallet.CreateWalletState
import org.sajad.shahabzi.features.createwallet.VerifyAnswers
import org.sajad.shahabzi.resources.Res
import org.sajad.shahabzi.resources.create_wallet_back
import org.sajad.shahabzi.resources.create_wallet_saving
import org.sajad.shahabzi.resources.create_wallet_verify_button
import org.sajad.shahabzi.resources.create_wallet_verify_subtitle
import org.sajad.shahabzi.resources.create_wallet_verify_title
import org.sajad.shahabzi.resources.create_wallet_word_label
import org.sajad.shahabzi.theme.AppDimens

@Composable
internal fun VerifyStep(
    state: CreateWalletState,
    onIntent: (CreateWalletIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Typed words stay in local UI state only: never in the ViewModel state, never saved.
    val answers = remember(state.challengePositions) { mutableStateMapOf<Int, String>() }
    val allFilled = state.challengePositions.all { answers[it].orEmpty().isNotBlank() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing),
    ) {
        Text(
            text = stringResource(Res.string.create_wallet_verify_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(Res.string.create_wallet_verify_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        state.challengePositions.forEach { position ->
            OutlinedTextField(
                value = answers[position].orEmpty(),
                onValueChange = { answers[position] = it },
                label = { Text(stringResource(Res.string.create_wallet_word_label, position)) },
                singleLine = true,
                enabled = !state.isSaving,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        state.error?.let { CreateWalletErrorText(it) }
        Button(
            onClick = { onIntent(CreateWalletIntent.VerifyClicked(VerifyAnswers(answers.toMap()))) },
            enabled = allFilled && !state.isSaving,
            modifier = Modifier.fillMaxWidth().height(AppDimens.ButtonHeight),
        ) {
            Text(
                stringResource(
                    if (state.isSaving) Res.string.create_wallet_saving else Res.string.create_wallet_verify_button,
                ),
            )
        }
        OutlinedButton(
            onClick = { onIntent(CreateWalletIntent.BackToPhraseClicked) },
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth().height(AppDimens.ButtonHeight),
        ) {
            Text(stringResource(Res.string.create_wallet_back))
        }
    }
}