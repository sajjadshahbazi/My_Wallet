package org.sajad.shahabzi.domain.wallet.usecase

import kotlin.random.Random
import org.sajad.shahabzi.domain.common.DomainConstants
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength

/** Picks which word positions (1-based, ascending) the user must re-enter to prove the backup. */
class CreateBackupChallengeUseCase(
    private val random: Random,
) {
    operator fun invoke(length: MnemonicLength = MnemonicLength.WORDS_12): List<Int> =
        (1..length.wordCount)
            .shuffled(random)
            .take(DomainConstants.BACKUP_CHALLENGE_WORD_COUNT)
            .sorted()
}