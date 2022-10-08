package com.example.newsappmvvm.domain.usecases.news

import androidx.lifecycle.LiveData
import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.repository.NewsRepository

class GetSavedArticlesUseCase(private val newsRepository: NewsRepository) {

    operator fun invoke(): LiveData<List<Article>> {
        return newsRepository.getSavedArticles()
    }
}