package org.sajad.shahabzi.features.welcome

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.sajad.shahabzi.common.NavigationKeys
import org.sajad.shahabzi.navigation.FakeAppNavigator

class WelcomeViewModelTest {

    private val navigator = FakeAppNavigator()
    private val viewModel = WelcomeViewModel(navigator)

    @Test
    fun initialState_termsNotAccepted() {
        assertFalse(viewModel.uiState.value.isTermsAccepted)
    }

    @Test
    fun onIntent_termsToggled_updatesState() {
        viewModel.onIntent(WelcomeIntent.TermsToggled(true))

        assertTrue(viewModel.uiState.value.isTermsAccepted)
    }

    @Test
    fun onIntent_createWalletClicked_withoutTerms_doesNotNavigate() {
        viewModel.onIntent(WelcomeIntent.CreateWalletClicked)

        assertTrue(navigator.navigatedRoutes.isEmpty())
    }

    @Test
    fun onIntent_createWalletClicked_withTerms_navigatesToCreateWallet() {
        viewModel.onIntent(WelcomeIntent.TermsToggled(true))

        viewModel.onIntent(WelcomeIntent.CreateWalletClicked)

        assertEquals(listOf(NavigationKeys.CREATE_WALLET), navigator.navigatedRoutes)
    }

    @Test
    fun onIntent_importWalletClicked_withTerms_navigatesToImportWallet() {
        viewModel.onIntent(WelcomeIntent.TermsToggled(true))

        viewModel.onIntent(WelcomeIntent.ImportWalletClicked)

        assertEquals(listOf(NavigationKeys.IMPORT_WALLET), navigator.navigatedRoutes)
    }

    @Test
    fun onIntent_termsUntoggled_afterAccepting_blocksNavigation() {
        viewModel.onIntent(WelcomeIntent.TermsToggled(true))
        viewModel.onIntent(WelcomeIntent.TermsToggled(false))

        viewModel.onIntent(WelcomeIntent.CreateWalletClicked)

        assertTrue(navigator.navigatedRoutes.isEmpty())
    }
}