package org.sajad.shahabzi.domain.wallet.model


/**
 * A secret recovery phrase. The phrase is never part of [toString], so it cannot
 * leak through logs or state dumps. Call [reveal] only where it is strictly needed.
 */
class Mnemonic(private val phrase: String) {
    fun reveal(): String = phrase

    override fun toString(): String = "Mnemonic(***)"
}