package com.example.newsappmvvm.presentation.ui

/*import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsappmvvm.domain.usecases.news.*
import com.example.newsappmvvm.presentation.viewmodels.NewsViewModel

class NewsViewModelProviderFactory(
    val app: Application,
    val getBreakingNewsUseCase: GetBreakingNewsUseCase,
    val requestSearchNewsUseCase: RequestSearchNewsUseCase,
    val getSavedArticlesUseCase: GetSavedArticlesUseCase,
    val requestDeleteArticleUseCase: RequestDeleteArticleUseCase,
    val requestUpsertArticleUseCase: RequestUpsertArticleUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(
            app,
            getBreakingNewsUseCase,
            requestSearchNewsUseCase,
            getSavedArticlesUseCase,
            requestDeleteArticleUseCase,
            requestUpsertArticleUseCase
        ) as T
    }
}*/