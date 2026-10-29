package org.sajad.shahabzi.testing

import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.service.AddressDeriver

internal class FakeAddressDeriver : AddressDeriver {
    override fun deriveDefaultAddress(mnemonic: Mnemonic, chain: Chain): AppResult<String> =
        AppResult.Success(ADDRESS)

    companion object {
        const val ADDRESS = "0xABCdef"
    }
}