package org.sajad.shahabzi.data.wallet

import kotlinx.coroutines.withContext
import org.sajad.shahabzi.data.common.DataConstants
import org.sajad.shahabzi.data.storage.KeyValueStore
import org.sajad.shahabzi.data.storage.SecretStore
import org.sajad.shahabzi.data.storage.StorageException
import org.sajad.shahabzi.domain.common.DispatcherProvider
import org.sajad.shahabzi.domain.common.error.AppError
import org.sajad.shahabzi.domain.common.result.AppResult
import org.sajad.shahabzi.domain.wallet.model.Chain
import org.sajad.shahabzi.domain.wallet.model.Mnemonic
import org.sajad.shahabzi.domain.wallet.model.Wallet
import org.sajad.shahabzi.domain.wallet.model.WalletAddress
import org.sajad.shahabzi.domain.wallet.repository.WalletRepository

internal class WalletRepositoryImpl(
    private val secretStore: SecretStore,
    private val keyValueStore: KeyValueStore,
    private val dispatchers: DispatcherProvider,
) : WalletRepository {

    override suspend fun save(wallet: Wallet, mnemonic: Mnemonic): AppResult<Unit> =
        withContext(dispatchers.io) {
            try {
                val ids = readWalletIds()
                if (wallet.id in ids) {
                    AppResult.Failure(AppError.WalletAlreadyExists)
                } else {
                    persist(wallet, mnemonic, ids)
                    AppResult.Success(Unit)
                }
            } catch (e: StorageException) {
                AppResult.Failure(AppError.StorageFailed)
            }
        }

    override suspend fun getWallets(): AppResult<List<Wallet>> =
        withContext(dispatchers.io) {
            try {
                val wallets = readWalletIds().map { id ->
                    val addresses = Chain.entries.mapNotNull { chain ->
                        keyValueStore.getString(addressKey(id, chain))
                            ?.let { WalletAddress(chain, it) }
                    }
                    Wallet(id = id, addresses = addresses)
                }
                AppResult.Success(wallets)
            } catch (e: StorageException) {
                AppResult.Failure(AppError.StorageFailed)
            }
        }

    /** Secret first, then metadata. If metadata fails, everything written is rolled back. */
    private fun persist(wallet: Wallet, mnemonic: Mnemonic, existingIds: List<String>) {
        val secretKey = DataConstants.KEY_PREFIX_WALLET_MNEMONIC + wallet.id
        secretStore.put(secretKey, mnemonic.reveal())
        try {
            wallet.addresses.forEach {
                keyValueStore.putString(addressKey(wallet.id, it.chain), it.address)
            }
            keyValueStore.putString(
                DataConstants.KEY_STORAGE_VERSION,
                DataConstants.STORAGE_VERSION.toString(),
            )
            keyValueStore.putString(
                DataConstants.KEY_WALLET_IDS,
                (existingIds + wallet.id).joinToString(DataConstants.ID_SEPARATOR),
            )
        } catch (e: StorageException) {
            wallet.addresses.forEach { keyValueStore.remove(addressKey(wallet.id, it.chain)) }
            secretStore.remove(secretKey)
            throw e
        }
    }

    private fun readWalletIds(): List<String> =
        keyValueStore.getString(DataConstants.KEY_WALLET_IDS)
            ?.split(DataConstants.ID_SEPARATOR)
            ?.filter { it.isNotEmpty() }
            ?: emptyList()

    private fun addressKey(walletId: String, chain: Chain): String =
        DataConstants.KEY_PREFIX_WALLET_ADDRESS + walletId + DataConstants.KEY_SEPARATOR + chain.id
}