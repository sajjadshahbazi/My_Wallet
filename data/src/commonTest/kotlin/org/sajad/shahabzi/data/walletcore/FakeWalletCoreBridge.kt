package org.sajad.shahabzi.data.walletcore

internal class FakeWalletCoreBridge(
    var generatedMnemonic: String? = VALID_PHRASE,
    var validMnemonics: Set<String> = setOf(VALID_PHRASE),
    var address: String? = ADDRESS,
) : WalletCoreBridge {

    var lastStrengthBits: Int? = null

    override fun generateMnemonic(strengthBits: Int): String? {
        lastStrengthBits = strengthBits
        return generatedMnemonic
    }

    override fun isValidMnemonic(mnemonic: String): Boolean = mnemonic in validMnemonics

    override fun deriveAddress(mnemonic: String, coin: String): String? = address

    companion object {
        const val VALID_PHRASE = "fake valid phrase"
        const val ADDRESS = "0xFakeAddress"
    }
}