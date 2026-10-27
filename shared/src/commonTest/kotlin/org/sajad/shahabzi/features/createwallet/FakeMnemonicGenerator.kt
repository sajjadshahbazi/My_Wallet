package org.sajad.shahabzi.features.createwallet

import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength
import org.sajad.shahabzi.domain.wallet.service.MnemonicGenerator

internal class FakeMnemonicGenerator(
    var result: AppResult<Mnemonic> = AppResult.Success(Mnemonic(PHRASE)),
) : MnemonicGenerator {

    var callCount = 0

    override fun generate(length: MnemonicLength): AppResult<Mnemonic> {
        callCount++
        return result
    }

    companion object {
        const val PHRASE = "fake words one two three four five six seven eight nine ten"
        val FAILURE = AppResult.Failure(AppError.MnemonicGenerationFailed)
    }
}