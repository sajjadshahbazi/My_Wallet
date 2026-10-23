package org.sajad.shahabzi.data.di

import org.koin.dsl.module
import org.sajad.shahabzi.data.common.DefaultDispatcherProvider
import org.sajad.shahabzi.domain.common.DispatcherProvider

val dataModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}