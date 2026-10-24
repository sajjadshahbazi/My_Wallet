package org.sajad.shahabzi.domain.wallet.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class MnemonicTest {

    @Test
    fun toString_doesNotRevealPhrase() {
        val mnemonic = Mnemonic("alpha beta gamma")

        assertFalse(mnemonic.toString().contains("alpha"))
    }

    @Test
    fun reveal_returnsOriginalPhrase() {
        assertEquals("alpha beta gamma", Mnemonic("alpha beta gamma").reveal())
    }
}