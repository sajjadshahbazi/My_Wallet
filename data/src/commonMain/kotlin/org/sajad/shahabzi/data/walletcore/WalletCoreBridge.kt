package org.sajad.shahabzi.data.walletcore

/**
 * Thin native bridge to Trust Wallet Core. Implemented natively on each platform
 * (Android: JNI, iOS: Swift). Uses only primitive types on purpose.
 *
 * Coin identifiers: "ETHEREUM".
 */
interface WalletCoreBridge {
    /** Returns a new mnemonic with [wordCount] words (12 or 24), or null on failure. */
    fun generateMnemonic(wordCount: Int): String?

    fun isValidMnemonic(mnemonic: String): Boolean

    /** Returns the default account address for [coin], or null on failure. */
    fun deriveAddress(mnemonic: String, coin: String): String?
}