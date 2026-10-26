package org.sajad.shahabzi.data.walletcore

/**
 * Thin native bridge to Trust Wallet Core. Implemented natively on each platform
 * (Android: JNI, iOS: Swift). Uses only primitive types on purpose.
 *
 * Coin identifiers are the COIN_* values of DataConstants.
 */
interface WalletCoreBridge {
    /** Returns a new mnemonic generated with [strengthBits] bits of entropy, or null on failure. */
    fun generateMnemonic(strengthBits: Int): String?

    fun isValidMnemonic(mnemonic: String): Boolean

    /** Returns the default account address for [coin], or null on failure. */
    fun deriveAddress(mnemonic: String, coin: String): String?
}