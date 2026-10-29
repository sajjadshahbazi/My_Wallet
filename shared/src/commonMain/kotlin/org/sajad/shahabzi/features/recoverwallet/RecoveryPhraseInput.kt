package org.sajad.shahabzi.features.recoverwallet

import org.sajad.shahabzi.domain.common.DomainConstants

/** The phrase typed by the user. Redacted in [toString] so it can never leak into logs. */
class RecoveryPhraseInput(val phrase: String) {
    override fun toString(): String = "RecoveryPhraseInput(${DomainConstants.REDACTED_PLACEHOLDER})"
}