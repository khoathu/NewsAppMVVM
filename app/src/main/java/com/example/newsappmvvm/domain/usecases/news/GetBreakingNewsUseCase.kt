package com.example.newsappmvvm.domain.usecases.news

import com.example.newsappmvvm.domain.repository.NewsRepository

class GetBreakingNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) {
        newsRepository.getBreakingNews(countryCode, pageNumber)
    }
}