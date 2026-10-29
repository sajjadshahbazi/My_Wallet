package org.sajad.shahabzi.testing

import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.Wallet
import org.sajad.shahabzi.domain.wallet.repository.WalletRepository

internal class FakeWalletRepository : WalletRepository {
    var saveResult: AppResult<Unit> = AppResult.Success(Unit)
    var walletsResult: AppResult<List<Wallet>> = AppResult.Success(emptyList())
    val saved = mutableListOf<Pair<Wallet, Mnemonic>>()

    override suspend fun save(wallet: Wallet, mnemonic: Mnemonic): AppResult<Unit> {
        saved += wallet to mnemonic
        return saveResult
    }

    override suspend fun getWallets(): AppResult<List<Wallet>> = walletsResult
}