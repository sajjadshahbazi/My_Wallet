package org.sajad.shahabzi.domain.wallet.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Wallet

class GetWalletsUseCaseTest {

    @Test
    fun invoke_returnsWalletsFromRepository() = runTest {
        val repository = FakeWalletRepository()
        val wallets = listOf(Wallet(id = "0xabc", addresses = emptyList()))
        repository.walletsResult = AppResult.Success(wallets)

        val result = GetWalletsUseCase(repository)()

        assertEquals(AppResult.Success(wallets), result)
    }
}