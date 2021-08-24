package com.soten.memo

import android.app.Application
import android.content.Context
import com.soten.memo.di.appModule
import com.soten.memo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin {
            androidContext(this@MemoApplication)
            modules(appModule + viewModelModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        var appContext: Context? = null
            private set
    }

}