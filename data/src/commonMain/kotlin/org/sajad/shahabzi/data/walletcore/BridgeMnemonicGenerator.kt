package org.sajad.shahabzi.data.walletcore

import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength
import org.sajad.shahabzi.domain.wallet.service.MnemonicGenerator

internal class BridgeMnemonicGenerator(
    private val bridge: WalletCoreBridge,
) : MnemonicGenerator {

    override fun generate(length: MnemonicLength): AppResult<Mnemonic> {
        val phrase = bridge.generateMnemonic(length.wordCount)
        return if (phrase.isNullOrBlank() || !bridge.isValidMnemonic(phrase)) {
            AppResult.Failure(AppError.MnemonicGenerationFailed)
        } else {
            AppResult.Success(Mnemonic(phrase))
        }
    }
}