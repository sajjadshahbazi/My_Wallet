package org.sajad.shahabzi.domain.wallet.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.Wallet
import org.sajad.shahabzi.domain.wallet.model.WalletAddress

class CreateWalletUseCaseTest {

    private val deriver = FakeAddressDeriver()
    private val repository = FakeWalletRepository()
    private val useCase = CreateWalletUseCase(deriver, repository)
    private val mnemonic = Mnemonic("fake phrase")

    @Test
    fun invoke_whenAllSucceeds_savesWalletWithLowercaseEthereumIdAndReturnsIt() = runTest {
        val expected = Wallet(
            id = "0xabcdef",
            addresses = listOf(WalletAddress(Chain.ETHEREUM, FakeAddressDeriver.ADDRESS)),
        )

        val result = useCase(mnemonic)

        assertEquals(AppResult.Success(expected), result)
        assertEquals(listOf(expected), repository.saved.map { it.first })
    }

    @Test
    fun invoke_whenDerivationFails_returnsFailureAndDoesNotSave() = runTest {
        deriver.result = AppResult.Failure(AppError.InvalidMnemonic)

        val result = useCase(mnemonic)

        assertEquals(AppResult.Failure(AppError.InvalidMnemonic), result)
        assertTrue(repository.saved.isEmpty())
    }

    @Test
    fun invoke_whenSaveFails_returnsThatFailure() = runTest {
        repository.saveResult = AppResult.Failure(AppError.WalletAlreadyExists)

        val result = useCase(mnemonic)

        assertEquals(AppResult.Failure(AppError.WalletAlreadyExists), result)
    }
}