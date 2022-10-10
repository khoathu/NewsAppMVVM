package com.example.newsappmvvm.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.newsappmvvm.data.api.NewsAPI
import com.example.newsappmvvm.data.db.ArticleDao
import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.model.NewsResponse
import com.example.newsappmvvm.domain.repository.NewsRepository
import com.example.newsappmvvm.utils.Resource
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsAPI,
    private val articleDao: ArticleDao
) : NewsRepository {

    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Resource<NewsResponse> {
        try {
            val response = newsApi.getBreakingNews(countryCode, pageNumber)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    return Resource.Success(resultResponse.map())
                }
            } else {
                return Resource.Error(response.message())
            }
        } catch (t: Throwable) {
            return when (t) {
                is IOException -> Resource.Error("Network failure")
                else -> Resource.Error(t.message.toString())
            }
        }
        return Resource.Error("Conversion Error")
    }


    override suspend fun searchNews(query: String, pageNumber: Int): Resource<NewsResponse> {
        try {
            val response = newsApi.searchForNews(query, pageNumber)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    return Resource.Success(resultResponse.map())
                }
            } else {
                return Resource.Error(response.message())
            }
        } catch (t: Throwable) {
            return when (t) {
                is IOException -> Resource.Error("Network failure")
                else -> Resource.Error("Conversion Error")
            }
        }
        return Resource.Error("Conversion Error")
    }

    override suspend fun upsertArticle(article: Article) =
        articleDao.upsert(article.map())

    override suspend fun deleteArticle(article: Article) =
        articleDao.deleteArticle(article.map())

    override fun getFavoriteArticles(): LiveData<List<Article>> =
        Transformations.map(articleDao.getFavoriteArticles()) { list ->
            list.map { item -> item.map() }
        }

    fun getLocalBreakingNews(): List<Article> = articleDao.getLocalBreakingNews().map { it.map() }

}