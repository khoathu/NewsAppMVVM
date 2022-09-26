package com.example.newsappmvvm.viewmodels

import androidx.lifecycle.ViewModel
import com.example.newsappmvvm.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {
}