package org.sajad.shahabzi

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.mp.KoinPlatform
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength
import org.sajad.shahabzi.domain.wallet.service.AddressDeriver
import org.sajad.shahabzi.domain.wallet.service.MnemonicGenerator

@RunWith(AndroidJUnit4::class)
class WalletCoreAndroidTest {

    private val generator = KoinPlatform.getKoin().get<MnemonicGenerator>()
    private val deriver = KoinPlatform.getKoin().get<AddressDeriver>()

    @Test
    fun deriveDefaultAddress_withKnownTestVector_returnsExpectedEthereumAddress() {
        // Public BIP-39 test vector. NOT a secret. Never use a real mnemonic here.
        val mnemonic = Mnemonic(
            "abandon abandon abandon abandon abandon abandon " +
                    "abandon abandon abandon abandon abandon about",
        )

        val result = deriver.deriveDefaultAddress(mnemonic, Chain.ETHEREUM)

        assertEquals(AppResult.Success("0x9858EfFD232B4033E47d90003D41EC34EcaEda94"), result)
    }

    @Test
    fun generate_with12Words_returnsMnemonicWithTwelveWords() {
        val result = generator.generate(MnemonicLength.WORDS_12)

        assertTrue(result is AppResult.Success)
        val mnemonic = (result as AppResult.Success).value
        assertEquals(12, mnemonic.reveal().split(" ").size)
        assertTrue(deriver.deriveDefaultAddress(mnemonic, Chain.ETHEREUM) is AppResult.Success)
    }

    @Test
    fun generate_twice_returnsDifferentMnemonics() {
        val first = (generator.generate(MnemonicLength.WORDS_12) as AppResult.Success).value
        val second = (generator.generate(MnemonicLength.WORDS_12) as AppResult.Success).value

        assertNotEquals(first.reveal(), second.reveal())
    }

    @Test
    fun deriveDefaultAddress_withInvalidMnemonic_returnsInvalidMnemonicError() {
        val result = deriver.deriveDefaultAddress(Mnemonic("not a valid phrase"), Chain.ETHEREUM)

        assertEquals(AppResult.Failure(AppError.InvalidMnemonic), result)
    }
}