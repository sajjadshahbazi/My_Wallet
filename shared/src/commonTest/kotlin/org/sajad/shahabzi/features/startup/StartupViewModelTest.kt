package org.sajad.shahabzi.features.startup

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.sajad.shahabzi.common.NavigationKeys
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Wallet
import org.sajad.shahabzi.domain.wallet.usecase.GetWalletsUseCase
import org.sajad.shahabzi.navigation.FakeAppNavigator
import org.sajad.shahabzi.testing.FakeWalletRepository
import org.sajad.shahabzi.testing.MainDispatcherHelper

class StartupViewModelTest {

    private val mainDispatcher = MainDispatcherHelper()
    private val repository = FakeWalletRepository()
    private val navigator = FakeAppNavigator()

    @BeforeTest
    fun setUp() {
        mainDispatcher.install()
    }

    @AfterTest
    fun tearDown() {
        mainDispatcher.uninstall()
    }

    private fun createViewModel() = StartupViewModel(GetWalletsUseCase(repository), navigator)

    @Test
    fun init_withNoWallets_navigatesToWelcomeAndPopsStartup() {
        createViewModel()

        assertEquals(
            listOf(FakeAppNavigator.NavigateCall(NavigationKeys.WELCOME, clearBackStack = true)),
            navigator.calls,
        )
    }

    @Test
    fun init_withWallet_navigatesToHomeAndPopsStartup() {
        repository.walletsResult = AppResult.Success(listOf(Wallet("0xabc", emptyList())))

        createViewModel()

        assertEquals(
            listOf(FakeAppNavigator.NavigateCall(NavigationKeys.HOME, clearBackStack = true)),
            navigator.calls,
        )
    }

    @Test
    fun init_whenStorageFails_showsErrorAndDoesNotNavigate() {
        repository.walletsResult = AppResult.Failure(AppError.StorageFailed)

        val viewModel = createViewModel()

        assertEquals(StartupError.StorageUnavailable, viewModel.uiState.value.error)
        assertEquals(emptyList(), navigator.calls)
    }

    @Test
    fun onIntent_retryClicked_afterFailure_navigatesWhenStorageRecovers() {
        repository.walletsResult = AppResult.Failure(AppError.StorageFailed)
        val viewModel = createViewModel()
        repository.walletsResult = AppResult.Success(emptyList())

        viewModel.onIntent(StartupIntent.RetryClicked)

        assertNull(viewModel.uiState.value.error)
        assertEquals(1, navigator.calls.size)
    }
}