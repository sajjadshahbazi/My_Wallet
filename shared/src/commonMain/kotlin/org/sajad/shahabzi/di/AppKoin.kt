package org.sajad.shahabzi.di


import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.sajad.shahabzi.data.di.dataModule

private val appModules = listOf(dataModule)

fun startAppKoin(appDeclaration: KoinAppDeclaration) {
    startKoin {
        appDeclaration()
        modules(appModules)
    }
}

fun startAppKoin() = startAppKoin {}