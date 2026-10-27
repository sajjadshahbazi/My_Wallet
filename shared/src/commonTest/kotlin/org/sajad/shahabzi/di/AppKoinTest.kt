package org.sajad.shahabzi.di

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.stopKoin
import org.koin.mp.KoinPlatform
import org.sajad.shahabzi.domain.common.DispatcherProvider
import org.sajad.shahabzi.navigation.AppNavigator


class AppKoinTest {

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun startAppKoin_resolvesDispatcherProvider() {
        startAppKoin()

        val provider = KoinPlatform.getKoin().get<DispatcherProvider>()

        assertNotNull(provider)
    }

    @Test
    fun startAppKoin_resolvesAppNavigator() {
        startAppKoin()

        val navigator = KoinPlatform.getKoin().get<AppNavigator>()

        assertNotNull(navigator)
    }
}