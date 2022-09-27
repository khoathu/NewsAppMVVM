package com.example.newsappmvvm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsappmvvm.repository.NewsRepository
import com.example.newsappmvvm.viewmodels.NewsViewModel

class NewsViewModelProviderFactory(
    val newsRepository: NewsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}