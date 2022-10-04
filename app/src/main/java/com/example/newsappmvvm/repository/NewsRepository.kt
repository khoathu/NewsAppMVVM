package com.example.newsappmvvm.repository

import com.example.newsappmvvm.api.NewsAPI
import com.example.newsappmvvm.db.ArticleDatabase
import com.example.newsappmvvm.models.Article

class NewsRepository(
    private val db: ArticleDatabase,
    private val api: NewsAPI
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(query: String, pageNumber: Int) =
        api.searchForNews(query, pageNumber)

    suspend fun upsertArticle(article: Article) =
        db.getArticleDao().upsert(article)

    suspend fun deleteArticle(article: Article) =
        db.getArticleDao().deleteArticle(article)

    fun getSavedArticles() =
        db.getArticleDao().getAllArticles()
}