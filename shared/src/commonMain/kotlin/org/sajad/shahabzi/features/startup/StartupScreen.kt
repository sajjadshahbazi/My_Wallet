package org.sajad.shahabzi.features.startup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.sajad.shahabzi.resources.Res
import org.sajad.shahabzi.resources.startup_error
import org.sajad.shahabzi.resources.startup_retry
import org.sajad.shahabzi.theme.AppDimens
import org.sajad.shahabzi.theme.AppTheme

@Composable
fun StartupScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<StartupViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    StartupContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun StartupContent(
    state: StartupState,
    onIntent: (StartupIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(AppDimens.ScreenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing, Alignment.CenterVertically),
    ) {
        if (state.error == null) {
            CircularProgressIndicator()
        } else {
            Text(
                text = stringResource(Res.string.startup_error),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
            )
            Button(onClick = { onIntent(StartupIntent.RetryClicked) }) {
                Text(stringResource(Res.string.startup_retry))
            }
        }
    }
}

@Preview
@Composable
private fun StartupContentLoadingPreview() {
    AppTheme { StartupContent(state = StartupState(), onIntent = {}) }
}

@Preview
@Composable
private fun StartupContentErrorPreview() {
    AppTheme {
        StartupContent(
            state = StartupState(error = StartupError.StorageUnavailable),
            onIntent = {},
        )
    }
}