package com.example.newsappmvvm.domain.repository

import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.model.NewsResponse
import com.example.newsappmvvm.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Flow<Resource<NewsResponse>>

    suspend fun searchNews(query: String, pageNumber: Int): Flow<Resource<NewsResponse>>

    suspend fun upsertArticle(article: Article): Long

    suspend fun deleteArticle(article: Article)

    fun getFavoriteArticles(): Flow<List<Article>>

    suspend fun getLocalBreakingNewsResponse(): NewsResponse

    suspend fun deleteAllArticles()
}