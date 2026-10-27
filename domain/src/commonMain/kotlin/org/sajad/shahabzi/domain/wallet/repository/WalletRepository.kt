package org.sajad.shahabzi.domain.wallet.repository

import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.Wallet

interface WalletRepository {
    /**
     * Securely stores [mnemonic] and the public data of [wallet].
     * Fails with WalletAlreadyExists if the id exists, StorageFailed on storage errors.
     */
    suspend fun save(wallet: Wallet, mnemonic: Mnemonic): AppResult<Unit>

    suspend fun getWallets(): AppResult<List<Wallet>>
}