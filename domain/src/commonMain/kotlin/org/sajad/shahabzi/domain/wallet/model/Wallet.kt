package org.sajad.shahabzi.domain.wallet.model

/**
 * A wallet's public data. [id] is the wallet's Ethereum address in lowercase, so importing
 * the same recovery phrase twice produces the same id. Contains no secrets.
 */
data class Wallet(
    val id: String,
    val addresses: List<WalletAddress>,
)