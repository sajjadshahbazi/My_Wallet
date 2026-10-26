package org.sajad.shahabzi.data.walletcore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength

class BridgeMnemonicGeneratorTest {

    @Test
    fun generate_whenBridgeReturnsValidPhrase_returnsSuccess() {
        val generator = BridgeMnemonicGenerator(FakeWalletCoreBridge())

        val result = generator.generate(MnemonicLength.WORDS_12)

        assertIs<AppResult.Success<*>>(result)
        assertEquals(FakeWalletCoreBridge.VALID_PHRASE, (result.value as Mnemonic).reveal())
    }

    @Test
    fun generate_whenBridgeReturnsNull_returnsFailure() {
        val generator = BridgeMnemonicGenerator(FakeWalletCoreBridge(generatedMnemonic = null))

        val result = generator.generate(MnemonicLength.WORDS_12)

        assertEquals(AppResult.Failure(AppError.MnemonicGenerationFailed), result)
    }

    @Test
    fun generate_whenBridgeReturnsInvalidPhrase_returnsFailure() {
        val generator = BridgeMnemonicGenerator(
            FakeWalletCoreBridge(generatedMnemonic = "not valid", validMnemonics = emptySet()),
        )

        val result = generator.generate(MnemonicLength.WORDS_12)

        assertEquals(AppResult.Failure(AppError.MnemonicGenerationFailed), result)
    }

    @Test
    fun generate_with12Words_requests128BitsOfEntropy() {
        val bridge = FakeWalletCoreBridge()

        BridgeMnemonicGenerator(bridge).generate(MnemonicLength.WORDS_12)

        assertEquals(128, bridge.lastStrengthBits)
    }

    @Test
    fun generate_with24Words_requests256BitsOfEntropy() {
        val bridge = FakeWalletCoreBridge()

        BridgeMnemonicGenerator(bridge).generate(MnemonicLength.WORDS_24)

        assertEquals(256, bridge.lastStrengthBits)
    }
}