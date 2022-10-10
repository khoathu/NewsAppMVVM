package com.example.newsappmvvm.domain.usecases.news

import com.example.newsappmvvm.domain.model.NewsResponse
import com.example.newsappmvvm.domain.repository.NewsRepository
import com.example.newsappmvvm.utils.Resource
import javax.inject.Inject

class GetBreakingNewsUseCase @Inject constructor(private val newsRepository: NewsRepository) {

    suspend operator fun invoke(countryCode: String, pageNumber: Int): Resource<NewsResponse> {
        return newsRepository.getBreakingNews(countryCode, pageNumber)
    }
}