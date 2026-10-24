package org.sajad.shahabzi.data.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.sajad.shahabzi.data.walletcore.AndroidWalletCoreBridge
import org.sajad.shahabzi.data.walletcore.WalletCoreBridge

actual val platformDataModule: Module = module {
    single<WalletCoreBridge> { AndroidWalletCoreBridge() }
}