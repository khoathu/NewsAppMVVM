package com.example.newsappmvvm.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsappmvvm.repository.NewsRepository
import com.example.newsappmvvm.viewmodels.NewsViewModel

class NewsViewModelProviderFactory(
    val app: Application,
    val newsRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}