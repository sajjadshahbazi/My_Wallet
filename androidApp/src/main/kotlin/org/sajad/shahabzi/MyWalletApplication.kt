package org.sajad.shahabzi


import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.sajad.shahabzi.di.startAppKoin

class MyWalletApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        System.loadLibrary("TrustWalletCore")

        startAppKoin {
            androidContext(this@MyWalletApplication)
        }

    }
}