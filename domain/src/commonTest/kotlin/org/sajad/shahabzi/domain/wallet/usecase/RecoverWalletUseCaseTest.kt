package org.sajad.shahabzi.domain.wallet.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult

class RecoverWalletUseCaseTest {

    private val deriver = FakeAddressDeriver()
    private val repository = FakeWalletRepository()
    private val useCase = RecoverWalletUseCase(CreateWalletUseCase(deriver, repository))

    private val twelveWords = "alpha bravo charlie delta echo foxtrot golf hotel india juliet kilo lima"

    @Test
    fun invoke_withMessyInput_normalizesAndStoresThePhrase() = runTest {
        val messy = "  Alpha\nBRAVO  charlie delta echo foxtrot\tgolf hotel india juliet kilo lima  "

        val result = useCase(messy)

        assertTrue(result is AppResult.Success)
        assertEquals(twelveWords, repository.saved.single().second.reveal())
    }

    @Test
    fun invoke_with24Words_isAccepted() = runTest {
        val result = useCase(List(24) { "word$it" }.joinToString(" "))

        assertTrue(result is AppResult.Success)
    }

    @Test
    fun invoke_withWrongWordCount_returnsInvalidWordCountAndDoesNotSave() = runTest {
        val result = useCase("alpha bravo charlie")

        assertEquals(AppResult.Failure(AppError.InvalidWordCount), result)
        assertTrue(repository.saved.isEmpty())
    }

    @Test
    fun invoke_withBlankInput_returnsInvalidWordCount() = runTest {
        assertEquals(AppResult.Failure(AppError.InvalidWordCount), useCase("   "))
    }

    @Test
    fun invoke_whenDerivationRejectsPhrase_returnsInvalidMnemonic() = runTest {
        deriver.result = AppResult.Failure(AppError.InvalidMnemonic)

        val result = useCase(twelveWords)

        assertEquals(AppResult.Failure(AppError.InvalidMnemonic), result)
    }

    @Test
    fun invoke_whenWalletAlreadyExists_returnsAlreadyExists() = runTest {
        repository.saveResult = AppResult.Failure(AppError.WalletAlreadyExists)

        val result = useCase(twelveWords)

        assertEquals(AppResult.Failure(AppError.WalletAlreadyExists), result)
    }
}