package org.sajad.shahabzi.data.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.sajad.shahabzi.data.storage.AndroidKeyValueStore
import org.sajad.shahabzi.data.storage.AndroidKeystoreSecretStore
import org.sajad.shahabzi.data.storage.KeyValueStore
import org.sajad.shahabzi.data.storage.SecretStore
import org.sajad.shahabzi.data.walletcore.AndroidWalletCoreBridge
import org.sajad.shahabzi.data.walletcore.WalletCoreBridge

actual val platformDataModule: Module = module {
    single<WalletCoreBridge> { AndroidWalletCoreBridge() }
    single<SecretStore> { AndroidKeystoreSecretStore(get()) }
    single<KeyValueStore> { AndroidKeyValueStore(get()) }
}