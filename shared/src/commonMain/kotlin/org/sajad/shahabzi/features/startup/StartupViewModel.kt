package org.sajad.shahabzi.features.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sajad.shahabzi.common.NavigationKeys
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.usecase.GetWalletsUseCase
import org.sajad.shahabzi.navigation.AppNavigator
import org.sajad.shahabzi.tools.mvi.IntentHandler
import org.sajad.shahabzi.tools.mvi.Mvi
import org.sajad.shahabzi.tools.mvi.mvi

class StartupViewModel(
    private val getWallets: GetWalletsUseCase,
    private val appNavigator: AppNavigator,
) : ViewModel(),
    Mvi<StartupState, Nothing> by mvi(StartupState()),
    IntentHandler<StartupIntent> {

    init {
        load()
    }

    override fun onIntent(intent: StartupIntent) {
        when (intent) {
            StartupIntent.RetryClicked -> load()
        }
    }

    private fun load() {
        updateUiState { copy(error = null) }
        viewModelScope.launch {
            when (val result = getWallets()) {
                is AppResult.Success -> {
                    val route = if (result.value.isEmpty()) NavigationKeys.WELCOME else NavigationKeys.HOME
                    appNavigator.navigateTo(route, clearBackStack = true)
                }
                is AppResult.Failure -> updateUiState { copy(error = StartupError.StorageUnavailable) }
            }
        }
    }
}