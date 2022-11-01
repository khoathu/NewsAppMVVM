package com.example.newsappmvvm.data.repository

import android.util.Log
import com.example.newsappmvvm.data.api.NewsAPI
import com.example.newsappmvvm.data.db.ArticleDao
import com.example.newsappmvvm.data.db.ArticleDatabase
import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.model.NewsResponse
import com.example.newsappmvvm.domain.repository.NewsRepository
import com.example.newsappmvvm.utils.Resource
import com.example.newsappmvvm.utils.Utils.Companion.isNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsAPI,
    private val articleDao: ArticleDao
) : NewsRepository {

    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Flow<Resource<NewsResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = newsApi.getBreakingNews(countryCode, pageNumber)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    //save list to database
                    resultResponse.articles.let {
                        if (pageNumber == 1) {
                            //in case refresh delete all old data
                            articleDao.deleteAllBreakingArticles()
                            Log.e("NewsRepositoryImpl", "deleteAllBreakingArticles")
                        }
                        // save new data
                        articleDao.saveArticles(it)
                        Log.e("NewsRepositoryImpl", "saveArticles")
                    }
                    Log.e("NewsRepositoryImpl", "Success")
                    emit(Resource.Success(resultResponse.map()))
                }
            } else {
                emit(Resource.Error(response.errorBody()?.string() ?: "Can not parse error object"))
            }
        } catch (t: Throwable) {
            Log.e("NewsRepositoryImpl", "Throwable :" + t.message)
            emit(
                if (isNetworkError(t)) {
                    val response = getLocalBreakingNewsResponse()
                    if (response.articles.isNotEmpty() && pageNumber == 1) {
                        Resource.Success(response)
                    } else {
                        Resource.Error("No internet connection")
                    }
                } else {
                    Resource.Error(t.message.toString())
                }
            )
            /*return when (t) {
                is IOException -> Resource.Error("Network failure")
                else -> Resource.Error(t.message.toString())
            }*/
        }
        //emit(Resource.Error("Conversion Error"))
    }


    override suspend fun searchNews(query: String, pageNumber: Int): Flow<Resource<NewsResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = newsApi.searchForNews(query, pageNumber)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        emit(Resource.Success(resultResponse.map()))
                    }
                } else {
                    emit(Resource.Error(response.errorBody()?.string() ?: "Can not parse error object"))
                    //throw HttpException(response)
                    //{"status":"error","code":"rateLimited","message":"You have made too many requests recently. Developer accounts are limited to 100 requests over a 24 hour period (50 requests available every 12 hours). Please upgrade to a paid plan if you need more requests."}
                    //emit(Resource.Error(response.errorBody()?.string()?.contains("message")?.toString()?:"Can not parse error object"))
                }
            } catch (t: Throwable) {
                emit(
                    when (t) {
                        /*is HttpException -> {
                            if (t.code() == 429) {
                                Resource.Error(t.toString())
                                //Resource.Error("You have made too many requests recently. Developer accounts are limited to 100 requests over a 24 hour period (50 requests available every 12 hours). Please upgrade to a paid plan if you need more requests.")
                            } else {
                                Resource.Error(t.message())
                            }
                        }*/
                        is IOException -> Resource.Error("Network failure")
                        else -> Resource.Error("Conversion Error")
                    }
                )
            }
            //emit(Resource.Error("Conversion Error"))
        }

    override suspend fun upsertArticle(article: Article) =
        articleDao.upsert(article.map())

    override suspend fun deleteArticle(article: Article) =
        articleDao.deleteArticle(article.map())

    override fun getFavoriteArticles(): Flow<List<Article>> =
        articleDao.getFavoriteArticles().map { list ->
            list.map { it.map() }
        }
//        Transformations.map(articleDao.getFavoriteArticles()) { list ->
//            list.map { item -> item.map() }
//        }

    override suspend fun getLocalBreakingNewsResponse(): NewsResponse {
        val list = articleDao.getLocalBreakingNews().map { it.map() }.toMutableList()
        return NewsResponse(list, "ok", list.size)
    }

    override suspend fun deleteAllArticles() {
        articleDao.deleteAllArticles()
    }

}