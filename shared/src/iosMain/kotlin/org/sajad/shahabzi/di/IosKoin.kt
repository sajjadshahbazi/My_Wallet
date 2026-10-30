package org.sajad.shahabzi.di

import org.koin.dsl.module
import org.sajad.shahabzi.data.storage.KeyValueStoreBridge
import org.sajad.shahabzi.data.storage.SecretStoreBridge
import org.sajad.shahabzi.data.walletcore.WalletCoreBridge

fun startKoinIos(
    walletCoreBridge: WalletCoreBridge,
    secretStoreBridge: SecretStoreBridge,
    keyValueStoreBridge: KeyValueStoreBridge,
) {
    startAppKoin {
        modules(
            module {
                single<WalletCoreBridge> { walletCoreBridge }
                single<SecretStoreBridge> { secretStoreBridge }
                single<KeyValueStoreBridge> { keyValueStoreBridge }
            },
        )
    }
}