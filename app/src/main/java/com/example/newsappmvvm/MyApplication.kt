package com.example.newsappmvvm

import android.app.Application
import android.content.Context
import com.example.newsappmvvm.di.networkModule
import com.example.newsappmvvm.di.repositoryModule
import com.example.newsappmvvm.di.viewModelModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bind
import org.kodein.di.singleton

class MyApplication : Application(), DIAware {
    override val di: DI by DI.lazy {
        import(androidXModule(this@MyApplication))
        bind<Context>() with singleton { applicationContext }
        bind<MyApplication>() with singleton { MyApplication() }

//        bind<ArticleDatabase>() with singleton { ArticleDatabase(instance()) }
//        bind<NewsRepository>() with singleton { NewsRepository(instance()) }
//        bind<NewsViewModelProviderFactory>() with provider {
//            NewsViewModelProviderFactory(
//                instance(),
//                instance()
//            )
//        }
//        bind<NewsViewModel>() with provider { NewsViewModel(instance(), instance()) }
        //bindProvider {  NewsViewModel(instance(), instance())}

        import(databaseModule)
        import(repositoryModule)
        import(viewModelModule)
        import(networkModule)
    }

}