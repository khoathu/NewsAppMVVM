package com.example.newsappmvvm.domain.usecases.news

import com.example.newsappmvvm.domain.model.NewsResponse
import com.example.newsappmvvm.domain.repository.NewsRepository
import com.example.newsappmvvm.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RequestSearchNewsUseCase @Inject constructor(private val newsRepository: NewsRepository) {

    suspend operator fun invoke(query: String, pageNumber: Int): Flow<Resource<NewsResponse>> {
        return newsRepository.searchNews(query, pageNumber)
    }
}