package com.example.newsappmvvm

import android.app.Application
import android.content.Context
import com.example.newsappmvvm.db.ArticleDatabase
import com.example.newsappmvvm.repository.NewsRepository
import com.example.newsappmvvm.ui.NewsViewModelProviderFactory
import com.example.newsappmvvm.viewmodels.NewsViewModel
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule

class MyApplication : Application(), DIAware {
    override val di: DI by DI.lazy {
        import(androidXModule(this@MyApplication))

        bind<Context>() with singleton { applicationContext }
        bind<ArticleDatabase>() with singleton { ArticleDatabase(instance()) }
        bind<NewsRepository>() with singleton { NewsRepository(instance()) }
        bind<NewsViewModelProviderFactory>() with provider {
            NewsViewModelProviderFactory(
                instance(),
                instance()
            )
        }
        bind<NewsViewModel>() with provider { NewsViewModel(instance(), instance()) }
        //bindProvider {  NewsViewModel(instance(), instance())}
    }

}