package org.sajad.shahabzi.domain.wallet.service


import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength

interface MnemonicGenerator {
    /** Generates a new valid mnemonic using a secure random source. */
    fun generate(length: MnemonicLength): AppResult<Mnemonic>
}