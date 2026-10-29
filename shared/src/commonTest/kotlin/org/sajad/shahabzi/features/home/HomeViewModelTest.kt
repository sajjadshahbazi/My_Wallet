package org.sajad.shahabzi.features.home

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Wallet
import org.sajad.shahabzi.domain.wallet.model.WalletAddress
import org.sajad.shahabzi.domain.wallet.usecase.GetWalletsUseCase
import org.sajad.shahabzi.testing.FakeWalletRepository
import org.sajad.shahabzi.testing.MainDispatcherHelper

class HomeViewModelTest {

    private val mainDispatcher = MainDispatcherHelper()
    private val repository = FakeWalletRepository()

    @BeforeTest
    fun setUp() {
        mainDispatcher.install()
    }

    @AfterTest
    fun tearDown() {
        mainDispatcher.uninstall()
    }

    private fun createViewModel() = HomeViewModel(GetWalletsUseCase(repository))

    @Test
    fun init_withWallet_showsEthereumAddress() {
        repository.walletsResult = AppResult.Success(
            listOf(Wallet("0xabc", listOf(WalletAddress(Chain.ETHEREUM, "0xAbC")))),
        )

        val state = createViewModel().uiState.value

        assertEquals("0xAbC", state.address)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun init_withNoWallet_showsNoWalletError() {
        val state = createViewModel().uiState.value

        assertEquals(HomeError.NoWallet, state.error)
    }

    @Test
    fun init_whenStorageFails_showsLoadFailedError() {
        repository.walletsResult = AppResult.Failure(AppError.StorageFailed)

        val state = createViewModel().uiState.value

        assertEquals(HomeError.LoadFailed, state.error)
    }

    @Test
    fun onIntent_retryClicked_afterFailure_loadsAddress() {
        repository.walletsResult = AppResult.Failure(AppError.StorageFailed)
        val viewModel = createViewModel()
        repository.walletsResult = AppResult.Success(
            listOf(Wallet("0xabc", listOf(WalletAddress(Chain.ETHEREUM, "0xAbC")))),
        )

        viewModel.onIntent(HomeIntent.RetryClicked)

        assertEquals("0xAbC", viewModel.uiState.value.address)
        assertNull(viewModel.uiState.value.error)
    }
}