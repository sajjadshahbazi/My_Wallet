package org.sajad.shahabzi.domain.wallet.usecase

import org.sajad.shahabzi.domain.wallet.model.Mnemonic

class VerifyBackupUseCase {
    /** [answers] maps a 1-based word position to the word the user typed. */
    operator fun invoke(mnemonic: Mnemonic, answers: Map<Int, String>): Boolean {
        if (answers.isEmpty()) return false
        val words = mnemonic.reveal().split(' ')
        return answers.all { (position, answer) ->
            words.getOrNull(position - 1) == answer.trim().lowercase()
        }
    }
}