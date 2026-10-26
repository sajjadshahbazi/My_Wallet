package org.sajad.shahabzi.data.walletcore

import org.sajad.shahabzi.data.common.DataConstants
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import wallet.core.jni.Mnemonic as WalletCoreMnemonic

internal class AndroidWalletCoreBridge : WalletCoreBridge {

    init {
        System.loadLibrary(DataConstants.WALLET_CORE_LIBRARY_NAME)
    }

    override fun generateMnemonic(strengthBits: Int): String? =
        HDWallet(strengthBits, "").mnemonic()

    override fun isValidMnemonic(mnemonic: String): Boolean =
        WalletCoreMnemonic.isValid(mnemonic)

    override fun deriveAddress(mnemonic: String, coin: String): String? {
        val coinType = when (coin) {
            DataConstants.COIN_ETHEREUM -> CoinType.ETHEREUM
            else -> return null
        }
        if (!WalletCoreMnemonic.isValid(mnemonic)) return null
        return HDWallet(mnemonic, "").getAddressForCoin(coinType)
    }
}