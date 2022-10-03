package com.example.newsappmvvm

import android.app.Application
import com.example.newsappmvvm.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                listOf(
                    retrofitModule, apiModule, repositoryModule, viewModelModule, databaseModule
                )
            ).androidContext(this@MyApplication)
        }
    }
}