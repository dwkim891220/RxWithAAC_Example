package kr.co.sugarhill.rxwithviewmodel

import android.app.Application
import kr.co.sugarhill.rxwithviewmodel.di.nemoModule
import kr.co.sugarhill.rxwithviewmodel.di.nemoApiModule
import org.koin.android.ext.android.startKoin

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, nemoModule + nemoApiModule)
    }
}