package org.sajad.shahabzi.domain.wallet.usecase

import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Wallet
import org.sajad.shahabzi.domain.wallet.repository.WalletRepository

class GetWalletsUseCase(
    private val walletRepository: WalletRepository,
) {
    suspend operator fun invoke(): AppResult<List<Wallet>> = walletRepository.getWallets()
}