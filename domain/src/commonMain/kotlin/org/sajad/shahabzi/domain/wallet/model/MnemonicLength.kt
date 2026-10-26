package org.sajad.shahabzi.domain.wallet.model

import org.sajad.shahabzi.domain.common.DomainConstants

enum class MnemonicLength(val wordCount: Int) {
    WORDS_12(DomainConstants.MNEMONIC_WORD_COUNT_12),
    WORDS_24(DomainConstants.MNEMONIC_WORD_COUNT_24),
}