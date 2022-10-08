package com.example.newsappmvvm.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.newsappmvvm.data.api.RetrofitInstance
import com.example.newsappmvvm.data.db.ArticleDatabase
import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.model.NewsResponse
import com.example.newsappmvvm.domain.repository.NewsRepository
import com.example.newsappmvvm.utils.Resource
import java.io.IOException

class NewsRepositoryImpl(
    val db: ArticleDatabase
) : NewsRepository {

    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Resource<NewsResponse> {
        try {
            val response = RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
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
            val response = RetrofitInstance.api.searchForNews(query, pageNumber)
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
        db.getArticleDao().upsert(article.map())

    override suspend fun deleteArticle(article: Article) =
        db.getArticleDao().deleteArticle(article.map())

    override fun getSavedArticles(): LiveData<List<Article>> =
        Transformations.map(db.getArticleDao().getAllArticles()) { list ->
            list.map { it.map() }
        }
}