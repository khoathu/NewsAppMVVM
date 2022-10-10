package com.example.newsappmvvm.domain.usecases.news

import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.repository.NewsRepository
import javax.inject.Inject

class RequestUpsertArticleUseCase @Inject constructor(private val newsRepository: NewsRepository) {

    suspend operator fun invoke(article: Article): Long {
        return newsRepository.upsertArticle(article)
    }
}