package org.sajad.shahabzi.domain.wallet.usecase

import org.sajad.shahabzi.domain.common.DomainConstants
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength
import org.sajad.shahabzi.domain.wallet.model.Wallet

private val WhitespaceRegex = Regex("\\s+")

/**
 * Recovers a wallet from a typed or pasted recovery phrase: normalizes it (case, spaces, line breaks),
 * checks the word count, then creates and stores the wallet. A phrase that is not a valid BIP-39
 * mnemonic fails with InvalidMnemonic.
 */
class RecoverWalletUseCase(
    private val createWallet: CreateWalletUseCase,
) {
    private val supportedWordCounts = MnemonicLength.entries.map { it.wordCount }

    suspend operator fun invoke(phrase: String): AppResult<Wallet> {
        val words = phrase.trim().lowercase().split(WhitespaceRegex).filter { it.isNotEmpty() }
        if (words.size !in supportedWordCounts) {
            return AppResult.Failure(AppError.InvalidWordCount)
        }
        return createWallet(Mnemonic(words.joinToString(DomainConstants.MNEMONIC_WORD_SEPARATOR)))
    }
}