package org.sajad.shahabzi.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.sajad.shahabzi.features.createwallet.CreateWalletViewModel
import org.sajad.shahabzi.features.home.HomeViewModel
import org.sajad.shahabzi.features.startup.StartupViewModel
import org.sajad.shahabzi.features.welcome.WelcomeViewModel
import org.sajad.shahabzi.navigation.AppNavigator
import org.sajad.shahabzi.navigation.ChannelAppNavigator

val presentationModule = module {
    singleOf(::ChannelAppNavigator) bind AppNavigator::class
    viewModelOf(::StartupViewModel)
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::CreateWalletViewModel)
    viewModelOf(::HomeViewModel)
}