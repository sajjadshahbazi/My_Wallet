package org.sajad.shahabzi.domain.wallet.usecase

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.sajad.shahabzi.domain.wallet.model.Mnemonic

class VerifyBackupUseCaseTest {

    private val useCase = VerifyBackupUseCase()
    private val mnemonic = Mnemonic("alpha bravo charlie delta echo foxtrot")

    @Test
    fun invoke_withCorrectWords_returnsTrue() {
        assertTrue(useCase(mnemonic, mapOf(1 to "alpha", 4 to "delta")))
    }

    @Test
    fun invoke_ignoresCaseAndSurroundingSpaces() {
        assertTrue(useCase(mnemonic, mapOf(2 to "  BRAVO ")))
    }

    @Test
    fun invoke_withWrongWord_returnsFalse() {
        assertFalse(useCase(mnemonic, mapOf(1 to "alpha", 4 to "echo")))
    }

    @Test
    fun invoke_withNoAnswers_returnsFalse() {
        assertFalse(useCase(mnemonic, emptyMap()))
    }

    @Test
    fun invoke_withOutOfRangePosition_returnsFalse() {
        assertFalse(useCase(mnemonic, mapOf(99 to "alpha")))
    }
}