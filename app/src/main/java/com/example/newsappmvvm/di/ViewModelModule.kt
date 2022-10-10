package com.example.newsappmvvm.di

import android.app.Application
import com.example.newsappmvvm.domain.usecases.news.*
import com.example.newsappmvvm.presentation.viewmodels.NewsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {

    @Provides
    @Singleton
    fun provideNewsViewModel(
        app: Application,
        getBreakingNewsUseCase: GetBreakingNewsUseCase,
        requestSearchNewsUseCase: RequestSearchNewsUseCase,
        getSavedArticlesUseCase: GetSavedArticlesUseCase,
        requestDeleteArticleUseCase: RequestDeleteArticleUseCase,
        requestUpsertArticleUseCase: RequestUpsertArticleUseCase
    ): NewsViewModel {
        return NewsViewModel(
            app,
            getBreakingNewsUseCase,
            requestSearchNewsUseCase,
            getSavedArticlesUseCase,
            requestDeleteArticleUseCase,
            requestUpsertArticleUseCase
        )
    }
}