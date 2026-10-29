package org.sajad.shahabzi.features.createwallet

import org.sajad.shahabzi.domain.common.DomainConstants

/** Words typed by the user (position -> word). Redacted in [toString] so it can never leak into logs. */
class VerifyAnswers(val words: Map<Int, String>) {
    override fun toString(): String = "VerifyAnswers(${DomainConstants.REDACTED_PLACEHOLDER})"
}