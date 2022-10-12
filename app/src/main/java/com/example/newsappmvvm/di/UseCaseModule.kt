package com.example.newsappmvvm.di

import com.example.newsappmvvm.domain.repository.NewsRepository
import com.example.newsappmvvm.domain.usecases.news.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetBreakingNewsUseCase(newsRepository: NewsRepository): GetBreakingNewsUseCase {
        return GetBreakingNewsUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideRequestSearchNewsUseCase(newsRepository: NewsRepository): RequestSearchNewsUseCase {
        return RequestSearchNewsUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideGetSavedArticlesUseCase(newsRepository: NewsRepository): GetSavedArticlesUseCase {
        return GetSavedArticlesUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideRequestDeleteArticleUseCase(newsRepository: NewsRepository): RequestDeleteArticleUseCase {
        return RequestDeleteArticleUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideRequestUpsertArticleUseCase(newsRepository: NewsRepository): RequestUpsertArticleUseCase {
        return RequestUpsertArticleUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideRequestDeleteAllArticleUseCase(newsRepository: NewsRepository): RequestDeleteAllArticlesUseCase {
        return RequestDeleteAllArticlesUseCase(newsRepository)
    }
}