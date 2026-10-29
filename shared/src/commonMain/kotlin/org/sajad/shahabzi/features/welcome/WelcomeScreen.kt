package org.sajad.shahabzi.features.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.sajad.shahabzi.components.labeledcheckbox.LabeledCheckbox
import org.sajad.shahabzi.resources.Res
import org.sajad.shahabzi.resources.welcome_create_wallet
import org.sajad.shahabzi.resources.welcome_logo_text
import org.sajad.shahabzi.resources.welcome_recover_wallet
import org.sajad.shahabzi.resources.welcome_subtitle
import org.sajad.shahabzi.resources.welcome_terms_agreement
import org.sajad.shahabzi.resources.welcome_title
import org.sajad.shahabzi.theme.AppDimens
import org.sajad.shahabzi.theme.AppTheme

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<WelcomeViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    WelcomeContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun WelcomeContent(
    state: WelcomeState,
    onIntent: (WelcomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(AppDimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(AppDimens.ItemSpacing),
    ) {
        WelcomeHeader(modifier = Modifier.weight(1f))
        LabeledCheckbox(
            checked = state.isTermsAccepted,
            onCheckedChange = { onIntent(WelcomeIntent.TermsToggled(it)) },
            label = stringResource(Res.string.welcome_terms_agreement),
        )
        Button(
            onClick = { onIntent(WelcomeIntent.CreateWalletClicked) },
            enabled = state.isTermsAccepted,
            modifier = Modifier.fillMaxWidth().height(AppDimens.ButtonHeight),
        ) {
            Text(stringResource(Res.string.welcome_create_wallet))
        }
        OutlinedButton(
            onClick = { onIntent(WelcomeIntent.RecoverWalletClicked) },
            enabled = state.isTermsAccepted,
            modifier = Modifier.fillMaxWidth().height(AppDimens.ButtonHeight),
        ) {
            Text(stringResource(Res.string.welcome_recover_wallet))
        }
    }
}

@Composable
private fun WelcomeHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing, Alignment.CenterVertically),
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.LogoSize)
                .clip(RoundedCornerShape(AppDimens.LogoCorner))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(Res.string.welcome_logo_text),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Text(
            text = stringResource(Res.string.welcome_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(Res.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun WelcomeContentPreview() {
    AppTheme { WelcomeContent(state = WelcomeState(), onIntent = {}) }
}

@Preview
@Composable
private fun WelcomeContentTermsAcceptedPreview() {
    AppTheme { WelcomeContent(state = WelcomeState(isTermsAccepted = true), onIntent = {}) }
}