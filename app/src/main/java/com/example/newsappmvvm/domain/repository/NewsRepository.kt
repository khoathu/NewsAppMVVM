package com.example.newsappmvvm.domain.repository

import androidx.lifecycle.LiveData
import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.model.NewsResponse
import com.example.newsappmvvm.utils.Resource

interface NewsRepository {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Resource<NewsResponse>

    suspend fun searchNews(query: String, pageNumber: Int): Resource<NewsResponse>

    suspend fun upsertArticle(article: Article): Long

    suspend fun deleteArticle(article: Article)

    fun getFavoriteArticles(): LiveData<List<Article>>

    suspend fun getLocalBreakingNewsResponse(): NewsResponse

    suspend fun deleteAllArticles()
}