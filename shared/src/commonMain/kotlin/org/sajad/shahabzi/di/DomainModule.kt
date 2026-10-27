package org.sajad.shahabzi.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.sajad.shahabzi.domain.wallet.usecase.CreateWalletUseCase
import org.sajad.shahabzi.domain.wallet.usecase.GetWalletsUseCase
import org.sajad.shahabzi.domain.wallet.usecase.GenerateMnemonicUseCase

val domainModule = module {
    factoryOf(::GenerateMnemonicUseCase)
    factoryOf(::CreateWalletUseCase)
    factoryOf(::GetWalletsUseCase)
}