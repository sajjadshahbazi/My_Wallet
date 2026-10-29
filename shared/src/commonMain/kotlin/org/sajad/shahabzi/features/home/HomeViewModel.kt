package org.sajad.shahabzi.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.usecase.GetWalletsUseCase
import org.sajad.shahabzi.tools.mvi.IntentHandler
import org.sajad.shahabzi.tools.mvi.Mvi
import org.sajad.shahabzi.tools.mvi.mvi

class HomeViewModel(
    private val getWallets: GetWalletsUseCase,
) : ViewModel(),
    Mvi<HomeState, Nothing> by mvi(HomeState()),
    IntentHandler<HomeIntent> {

    init {
        load()
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.RetryClicked -> load()
        }
    }

    private fun load() {
        updateUiState { copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = getWallets()) {
                is AppResult.Success -> {
                    val address = result.value.firstOrNull()
                        ?.addresses
                        ?.firstOrNull { it.chain == Chain.ETHEREUM }
                        ?.address
                    if (address == null) {
                        updateUiState { copy(isLoading = false, error = HomeError.NoWallet) }
                    } else {
                        updateUiState { copy(isLoading = false, address = address) }
                    }
                }
                is AppResult.Failure ->
                    updateUiState { copy(isLoading = false, error = HomeError.LoadFailed) }
            }
        }
    }
}