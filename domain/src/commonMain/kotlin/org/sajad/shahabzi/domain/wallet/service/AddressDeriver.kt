package org.sajad.shahabzi.domain.wallet.service

import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Mnemonic

interface AddressDeriver {
    /**
     * Derives the default (first) account address of [chain] from [mnemonic].
     * Fails with InvalidMnemonic if the phrase is invalid.
     */
    fun deriveDefaultAddress(mnemonic: Mnemonic, chain: Chain): AppResult<String>
}