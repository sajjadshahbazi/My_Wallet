package org.sajad.shahabzi.data.di

import org.koin.dsl.module
import org.sajad.shahabzi.data.common.DefaultDispatcherProvider
import org.sajad.shahabzi.data.walletcore.BridgeAddressDeriver
import org.sajad.shahabzi.data.walletcore.BridgeMnemonicGenerator
import org.sajad.shahabzi.domain.common.DispatcherProvider
import org.sajad.shahabzi.domain.wallet.service.AddressDeriver
import org.sajad.shahabzi.domain.wallet.service.MnemonicGenerator

val dataModule = module {
    includes(platformDataModule)
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single<MnemonicGenerator> { BridgeMnemonicGenerator(get()) }
    single<AddressDeriver> { BridgeAddressDeriver(get()) }
}