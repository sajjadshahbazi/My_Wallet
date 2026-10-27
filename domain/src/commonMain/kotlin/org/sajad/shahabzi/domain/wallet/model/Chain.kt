package org.sajad.shahabzi.domain.wallet.model

import org.sajad.shahabzi.domain.common.DomainConstants

/** [id] is persisted in storage: never change an existing value. */
enum class Chain(val id: String) {
    ETHEREUM(DomainConstants.CHAIN_ID_ETHEREUM),
}