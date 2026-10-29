package org.sajad.shahabzi.domain.wallet.usecase

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.sajad.shahabzi.domain.wallet.model.MnemonicLength

class CreateBackupChallengeUseCaseTest {

    @Test
    fun invoke_returnsThreeDistinctAscendingPositionsInRange() {
        val positions = CreateBackupChallengeUseCase(Random(1))()

        assertEquals(3, positions.size)
        assertEquals(positions.sorted(), positions)
        assertEquals(positions.distinct(), positions)
        assertTrue(positions.all { it in 1..12 })
    }

    @Test
    fun invoke_withSameSeed_returnsSamePositions() {
        assertEquals(
            CreateBackupChallengeUseCase(Random(7))(),
            CreateBackupChallengeUseCase(Random(7))(),
        )
    }

    @Test
    fun invoke_with24Words_returnsPositionsUpTo24() {
        val positions = CreateBackupChallengeUseCase(Random(3))(MnemonicLength.WORDS_24)

        assertTrue(positions.all { it in 1..24 })
    }
}