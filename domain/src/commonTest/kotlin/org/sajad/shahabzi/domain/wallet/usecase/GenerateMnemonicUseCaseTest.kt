package org.sajad.shahabzi.domain.wallet.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength
import org.sajad.shahabzi.domain.wallet.service.MnemonicGenerator

class GenerateMnemonicUseCaseTest {

    @Test
    fun invoke_delegatesToGeneratorWithRequestedLength() {
        var requestedLength: MnemonicLength? = null
        val expected = AppResult.Success(Mnemonic("one two"))
        val useCase = GenerateMnemonicUseCase(
            object : MnemonicGenerator {
                override fun generate(length: MnemonicLength): AppResult<Mnemonic> {
                    requestedLength = length
                    return expected
                }
            },
        )

        val result = useCase(MnemonicLength.WORDS_24)

        assertEquals(MnemonicLength.WORDS_24, requestedLength)
        assertSame(expected, result)
    }
}