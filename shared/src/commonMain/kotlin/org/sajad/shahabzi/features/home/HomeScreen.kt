package org.sajad.shahabzi.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.sajad.shahabzi.resources.Res
import org.sajad.shahabzi.resources.home_add_wallet
import org.sajad.shahabzi.resources.home_address_label
import org.sajad.shahabzi.resources.home_dev_warning
import org.sajad.shahabzi.resources.home_error_load
import org.sajad.shahabzi.resources.home_error_no_wallet
import org.sajad.shahabzi.resources.home_retry
import org.sajad.shahabzi.resources.home_title
import org.sajad.shahabzi.theme.AppDimens
import org.sajad.shahabzi.theme.AppTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(AppDimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing),
    ) {
        Text(
            text = stringResource(Res.string.home_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        when {
            state.isLoading -> CircularProgressIndicator()
            state.error != null -> {
                Text(
                    text = stringResource(
                        if (state.error == HomeError.NoWallet) Res.string.home_error_no_wallet else Res.string.home_error_load,
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                )
                Button(onClick = { onIntent(HomeIntent.RetryClicked) }) {
                    Text(stringResource(Res.string.home_retry))
                }
            }
            else -> AddressCard(address = state.address.orEmpty())
        }

        OutlinedButton(
            onClick = { onIntent(HomeIntent.AddWalletClicked) },
            modifier = Modifier.fillMaxWidth().height(AppDimens.ButtonHeight),
        ) {
            Text(stringResource(Res.string.home_add_wallet))
        }

        Text(
            text = stringResource(Res.string.home_dev_warning),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun AddressCard(address: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.CardCorner),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(AppDimens.CardPadding),
            verticalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing),
        ) {
            Text(
                text = stringResource(Res.string.home_address_label),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            SelectionContainer {
                Text(
                    text = address,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    AppTheme { HomeContent(state = HomeState(isLoading = false, address = "0xA1b2…c3D4"), onIntent = {}) }
}

@Preview
@Composable
private fun HomeContentLoadingPreview() {
    AppTheme { HomeContent(state = HomeState(), onIntent = {}) }
}

@Preview
@Composable
private fun HomeContentErrorPreview() {
    AppTheme {
        HomeContent(
            state = HomeState(isLoading = false, error = HomeError.LoadFailed),
            onIntent = {},
        )
    }
}