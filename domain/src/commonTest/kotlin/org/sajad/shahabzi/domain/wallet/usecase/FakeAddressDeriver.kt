package org.sajad.shahabzi.domain.wallet.usecase

import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.service.AddressDeriver

internal class FakeAddressDeriver(
    var result: AppResult<String> = AppResult.Success(ADDRESS),
) : AddressDeriver {

    override fun deriveDefaultAddress(mnemonic: Mnemonic, chain: Chain): AppResult<String> = result

    companion object {
        const val ADDRESS = "0xABCdef"
    }
}