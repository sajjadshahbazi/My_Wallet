package org.sajad.shahabzi.domain.wallet.usecase

import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.Wallet
import org.sajad.shahabzi.domain.wallet.model.WalletAddress
import org.sajad.shahabzi.domain.wallet.repository.WalletRepository
import org.sajad.shahabzi.domain.wallet.service.AddressDeriver

/** Creates and stores a wallet from [Mnemonic]. Used for both new wallets and recovery (import). */
class CreateWalletUseCase(
    private val addressDeriver: AddressDeriver,
    private val walletRepository: WalletRepository,
) {
    suspend operator fun invoke(mnemonic: Mnemonic): AppResult<Wallet> {
        val addresses = mutableListOf<WalletAddress>()
        for (chain in Chain.entries) {
            when (val derived = addressDeriver.deriveDefaultAddress(mnemonic, chain)) {
                is AppResult.Success -> addresses += WalletAddress(chain, derived.value)
                is AppResult.Failure -> return derived
            }
        }
        val ethereumAddress = addresses.first { it.chain == Chain.ETHEREUM }.address
        val wallet = Wallet(id = ethereumAddress.lowercase(), addresses = addresses)
        return when (val saved = walletRepository.save(wallet, mnemonic)) {
            is AppResult.Success -> AppResult.Success(wallet)
            is AppResult.Failure -> saved
        }
    }
}