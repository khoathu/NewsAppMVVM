package com.example.newsappmvvm.presentation.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsappmvvm.data.repository.NewsRepositoryImpl
import com.example.newsappmvvm.presentation.viewmodels.NewsViewModel

class NewsViewModelProviderFactory(
    val app: Application,
    val newsRepository: NewsRepositoryImpl
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}