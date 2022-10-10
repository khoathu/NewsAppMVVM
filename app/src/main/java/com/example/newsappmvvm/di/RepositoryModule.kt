package com.example.newsappmvvm.di

import com.example.newsappmvvm.data.api.NewsAPI
import com.example.newsappmvvm.data.db.ArticleDao
import com.example.newsappmvvm.data.repository.NewsRepositoryImpl
import com.example.newsappmvvm.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideNewsRepository(newsApi: NewsAPI, articleDao: ArticleDao): NewsRepository {
        return NewsRepositoryImpl(newsApi, articleDao)
    }
}