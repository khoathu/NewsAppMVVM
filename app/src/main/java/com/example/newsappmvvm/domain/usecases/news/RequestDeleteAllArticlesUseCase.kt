package com.example.newsappmvvm.domain.usecases.news

import com.example.newsappmvvm.domain.repository.NewsRepository

class RequestDeleteAllArticlesUseCase(val newsRepository: NewsRepository) {

    suspend operator fun invoke() {
        newsRepository.deleteAllArticles()
    }
}