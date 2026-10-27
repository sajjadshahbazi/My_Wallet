package org.sajad.shahabzi.domain.wallet.usecase

import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength
import org.sajad.shahabzi.domain.wallet.service.MnemonicGenerator

class GenerateMnemonicUseCase(
    private val mnemonicGenerator: MnemonicGenerator,
) {
    operator fun invoke(length: MnemonicLength = MnemonicLength.WORDS_12): AppResult<Mnemonic> =
        mnemonicGenerator.generate(length)
}