package org.sajad.shahabzi.data.wallet

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.sajad.shahabzi.data.common.TestDispatcherProvider
import org.sajad.shahabzi.data.storage.FakeKeyValueStore
import org.sajad.shahabzi.data.storage.FakeSecretStore
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.Wallet
import org.sajad.shahabzi.domain.wallet.model.WalletAddress

class WalletRepositoryImplTest {

    private val secrets = FakeSecretStore()
    private val keyValues = FakeKeyValueStore()
    private val wallet = Wallet(
        id = "0xabc",
        addresses = listOf(WalletAddress(Chain.ETHEREUM, "0xAbC")),
    )
    private val mnemonic = Mnemonic("fake phrase")

    private fun repository(scheduler: TestCoroutineScheduler) = WalletRepositoryImpl(
        secretStore = secrets,
        keyValueStore = keyValues,
        dispatchers = TestDispatcherProvider(StandardTestDispatcher(scheduler)),
    )

    @Test
    fun save_storesSecretAndMetadataWithStableKeys() = runTest {
        val result = repository(testScheduler).save(wallet, mnemonic)

        assertEquals(AppResult.Success(Unit), result)
        assertEquals("fake phrase", secrets.values["wallet_mnemonic.0xabc"])
        assertEquals("0xAbC", keyValues.values["wallet_address.0xabc.ethereum"])
        assertEquals("0xabc", keyValues.values["wallet_ids"])
        assertEquals("1", keyValues.values["storage_version"])
    }

    @Test
    fun save_whenWalletExists_returnsAlreadyExistsAndKeepsOriginal() = runTest {
        val repository = repository(testScheduler)
        repository.save(wallet, mnemonic)

        val result = repository.save(wallet, Mnemonic("other phrase"))

        assertEquals(AppResult.Failure(AppError.WalletAlreadyExists), result)
        assertEquals("fake phrase", secrets.values["wallet_mnemonic.0xabc"])
    }

    @Test
    fun save_whenSecretStoreFails_returnsStorageFailedAndWritesNoMetadata() = runTest {
        secrets.failOnPut = true

        val result = repository(testScheduler).save(wallet, mnemonic)

        assertEquals(AppResult.Failure(AppError.StorageFailed), result)
        assertTrue(keyValues.values.isEmpty())
    }

    @Test
    fun save_whenMetadataFails_removesSecretAndReturnsStorageFailed() = runTest {
        keyValues.failOnPut = true

        val result = repository(testScheduler).save(wallet, mnemonic)

        assertEquals(AppResult.Failure(AppError.StorageFailed), result)
        assertTrue(secrets.values.isEmpty())
    }

    @Test
    fun getWallets_whenNothingSaved_returnsEmptyList() = runTest {
        val result = repository(testScheduler).getWallets()

        assertEquals(AppResult.Success(emptyList()), result)
    }

    @Test
    fun getWallets_returnsSavedWalletsWithAddresses() = runTest {
        val repository = repository(testScheduler)
        repository.save(wallet, mnemonic)

        val result = repository.getWallets()

        assertEquals(AppResult.Success(listOf(wallet)), result)
    }
}