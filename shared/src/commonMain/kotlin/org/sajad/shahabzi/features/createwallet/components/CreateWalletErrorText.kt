package org.sajad.shahabzi.features.createwallet.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import org.sajad.shahabzi.features.createwallet.CreateWalletError
import org.sajad.shahabzi.resources.Res
import org.sajad.shahabzi.resources.create_wallet_error
import org.sajad.shahabzi.resources.create_wallet_error_exists
import org.sajad.shahabzi.resources.create_wallet_error_save
import org.sajad.shahabzi.resources.create_wallet_error_verification

@Composable
internal fun CreateWalletErrorText(error: CreateWalletError, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(
            when (error) {
                CreateWalletError.GenerationFailed -> Res.string.create_wallet_error
                CreateWalletError.VerificationFailed -> Res.string.create_wallet_error_verification
                CreateWalletError.SaveFailed -> Res.string.create_wallet_error_save
                CreateWalletError.WalletAlreadyExists -> Res.string.create_wallet_error_exists
            },
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier,
    )
}