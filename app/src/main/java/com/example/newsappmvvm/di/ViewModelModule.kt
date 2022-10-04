package com.example.newsappmvvm.di

import com.example.newsappmvvm.viewmodels.NewsViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val viewModelModule = DI.Module(name = "view_model_module")
{
    bindProvider { NewsViewModel(app = instance(), newsRepository = instance()) }
}