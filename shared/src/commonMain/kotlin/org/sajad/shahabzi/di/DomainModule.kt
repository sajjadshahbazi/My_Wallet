package org.sajad.shahabzi.di

import kotlin.random.Random
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.sajad.shahabzi.domain.wallet.usecase.CreateBackupChallengeUseCase
import org.sajad.shahabzi.domain.wallet.usecase.CreateWalletUseCase
import org.sajad.shahabzi.domain.wallet.usecase.GenerateMnemonicUseCase
import org.sajad.shahabzi.domain.wallet.usecase.GetWalletsUseCase
import org.sajad.shahabzi.domain.wallet.usecase.VerifyBackupUseCase

val domainModule = module {
    // Not security-sensitive: only used to pick which words to quiz.
    single<Random> { Random.Default }
    factoryOf(::GenerateMnemonicUseCase)
    factoryOf(::CreateWalletUseCase)
    factoryOf(::GetWalletsUseCase)
    factoryOf(::CreateBackupChallengeUseCase)
    factoryOf(::VerifyBackupUseCase)
}