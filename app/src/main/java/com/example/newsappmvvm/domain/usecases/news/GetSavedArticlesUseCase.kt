package com.example.newsappmvvm.domain.usecases.news

import androidx.lifecycle.LiveData
import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedArticlesUseCase @Inject constructor(private val newsRepository: NewsRepository) {

    operator fun invoke(): Flow<List<Article>> {
        return newsRepository.getFavoriteArticles()
    }
}