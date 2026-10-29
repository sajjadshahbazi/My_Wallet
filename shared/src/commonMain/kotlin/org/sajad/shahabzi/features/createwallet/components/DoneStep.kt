package org.sajad.shahabzi.features.createwallet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.stringResource
import org.sajad.shahabzi.features.createwallet.CreateWalletIntent
import org.sajad.shahabzi.features.createwallet.CreateWalletState
import org.sajad.shahabzi.resources.Res
import org.sajad.shahabzi.resources.create_wallet_done_address_label
import org.sajad.shahabzi.resources.create_wallet_done_button
import org.sajad.shahabzi.resources.create_wallet_done_subtitle
import org.sajad.shahabzi.resources.create_wallet_done_title
import org.sajad.shahabzi.theme.AppDimens

@Composable
internal fun DoneStep(
    state: CreateWalletState,
    onIntent: (CreateWalletIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing),
    ) {
        Text(
            text = stringResource(Res.string.create_wallet_done_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(Res.string.create_wallet_done_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppDimens.CardCorner),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.padding(AppDimens.CardPadding),
                verticalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing),
            ) {
                Text(
                    text = stringResource(Res.string.create_wallet_done_address_label),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                SelectionContainer {
                    Text(
                        text = state.walletAddress.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
        Button(
            onClick = { onIntent(CreateWalletIntent.DoneClicked) },
            modifier = Modifier.fillMaxWidth().height(AppDimens.ButtonHeight),
        ) {
            Text(stringResource(Res.string.create_wallet_done_button))
        }
    }
}