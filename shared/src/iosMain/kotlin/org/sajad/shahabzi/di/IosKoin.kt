package org.sajad.shahabzi.di

import org.koin.dsl.module
import org.sajad.shahabzi.data.walletcore.WalletCoreBridge

fun startKoinIos(walletCoreBridge: WalletCoreBridge) {
    startAppKoin {
        modules(module { single<WalletCoreBridge> { walletCoreBridge } })
    }
}