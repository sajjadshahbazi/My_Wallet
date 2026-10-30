package org.sajad.shahabzi.data.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.sajad.shahabzi.data.storage.BridgeKeyValueStore
import org.sajad.shahabzi.data.storage.BridgeSecretStore
import org.sajad.shahabzi.data.storage.KeyValueStore
import org.sajad.shahabzi.data.storage.SecretStore

// The Swift bridges are registered at app start (see startKoinIos in the shared module).
actual val platformDataModule: Module = module {
    single<SecretStore> { BridgeSecretStore(get()) }
    single<KeyValueStore> { BridgeKeyValueStore(get()) }
}