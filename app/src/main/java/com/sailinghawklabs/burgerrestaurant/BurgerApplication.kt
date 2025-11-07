package com.sailinghawklabs.burgerrestaurant

import android.app.Application
import com.sailinghawklabs.burgerrestaurant.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class BurgerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BurgerApplication)
            modules(appModule)
        }
    }
}
