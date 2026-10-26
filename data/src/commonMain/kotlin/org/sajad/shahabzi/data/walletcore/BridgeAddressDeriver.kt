package org.sajad.shahabzi.data.walletcore

import org.sajad.shahabzi.data.common.DataConstants
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.service.AddressDeriver

internal class BridgeAddressDeriver(
    private val bridge: WalletCoreBridge,
) : AddressDeriver {

    override fun deriveDefaultAddress(mnemonic: Mnemonic, chain: Chain): AppResult<String> {
        val phrase = mnemonic.reveal()
        if (!bridge.isValidMnemonic(phrase)) {
            return AppResult.Failure(AppError.InvalidMnemonic)
        }
        val address = bridge.deriveAddress(phrase, chain.toBridgeCoin())
        return if (address.isNullOrBlank()) {
            AppResult.Failure(AppError.DerivationFailed)
        } else {
            AppResult.Success(address)
        }
    }
}

private fun Chain.toBridgeCoin(): String = when (this) {
    Chain.ETHEREUM -> DataConstants.COIN_ETHEREUM
}