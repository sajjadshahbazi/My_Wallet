package org.sajad.shahabzi.data.di

import org.koin.core.module.Module
import org.koin.dsl.module

// The iOS WalletCoreBridge is implemented in Swift and registered at app start
// (see startKoinIos in the shared module).
actual val platformDataModule: Module = module { }