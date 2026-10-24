package org.sajad.shahabzi.data.walletcore

import kotlin.test.Test
import kotlin.test.assertEquals
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Mnemonic

class BridgeAddressDeriverTest {

    @Test
    fun deriveDefaultAddress_whenValid_returnsAddress() {
        val deriver = BridgeAddressDeriver(FakeWalletCoreBridge())

        val result = deriver.deriveDefaultAddress(Mnemonic(FakeWalletCoreBridge.VALID_PHRASE), Chain.ETHEREUM)

        assertEquals(AppResult.Success(FakeWalletCoreBridge.ADDRESS), result)
    }

    @Test
    fun deriveDefaultAddress_whenMnemonicInvalid_returnsInvalidMnemonic() {
        val deriver = BridgeAddressDeriver(FakeWalletCoreBridge())

        val result = deriver.deriveDefaultAddress(Mnemonic("something else"), Chain.ETHEREUM)

        assertEquals(AppResult.Failure(AppError.InvalidMnemonic), result)
    }

    @Test
    fun deriveDefaultAddress_whenBridgeReturnsNull_returnsDerivationFailed() {
        val deriver = BridgeAddressDeriver(FakeWalletCoreBridge(address = null))

        val result = deriver.deriveDefaultAddress(Mnemonic(FakeWalletCoreBridge.VALID_PHRASE), Chain.ETHEREUM)

        assertEquals(AppResult.Failure(AppError.DerivationFailed), result)
    }
}