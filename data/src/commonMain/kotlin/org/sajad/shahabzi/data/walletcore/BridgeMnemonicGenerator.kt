package org.sajad.shahabzi.data.walletcore

import org.sajad.shahabzi.data.common.DataConstants
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength
import org.sajad.shahabzi.domain.wallet.service.MnemonicGenerator

internal class BridgeMnemonicGenerator(
    private val bridge: WalletCoreBridge,
) : MnemonicGenerator {

    override fun generate(length: MnemonicLength): AppResult<Mnemonic> {
        val phrase = bridge.generateMnemonic(length.toStrengthBits())
        return if (phrase.isNullOrBlank() || !bridge.isValidMnemonic(phrase)) {
            AppResult.Failure(AppError.MnemonicGenerationFailed)
        } else {
            AppResult.Success(Mnemonic(phrase))
        }
    }
}

private fun MnemonicLength.toStrengthBits(): Int = when (this) {
    MnemonicLength.WORDS_12 -> DataConstants.MNEMONIC_STRENGTH_BITS_12_WORDS
    MnemonicLength.WORDS_24 -> DataConstants.MNEMONIC_STRENGTH_BITS_24_WORDS
}