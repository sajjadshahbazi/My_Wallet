package org.sajad.shahabzi.data.walletcore

import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import wallet.core.jni.Mnemonic as WalletCoreMnemonic

internal class AndroidWalletCoreBridge : WalletCoreBridge {

    init {
        System.loadLibrary("TrustWalletCore")
    }

    override fun generateMnemonic(wordCount: Int): String? {
        val strength = when (wordCount) {
            12 -> 128
            24 -> 256
            else -> return null
        }
        return HDWallet(strength, "").mnemonic()
    }

    override fun isValidMnemonic(mnemonic: String): Boolean =
        WalletCoreMnemonic.isValid(mnemonic)

    override fun deriveAddress(mnemonic: String, coin: String): String? {
        val coinType = when (coin) {
            "ETHEREUM" -> CoinType.ETHEREUM
            else -> return null
        }
        if (!WalletCoreMnemonic.isValid(mnemonic)) return null
        return HDWallet(mnemonic, "").getAddressForCoin(coinType)
    }
}