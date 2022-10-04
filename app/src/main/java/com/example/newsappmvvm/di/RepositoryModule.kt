package com.example.newsappmvvm.di

import com.example.newsappmvvm.repository.NewsRepository
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val repositoryModule = DI.Module(name = "repository_module")
{
    bind<NewsRepository>() with singleton {
        NewsRepository(db = instance())
    }
}