package com.example.newsappmvvm.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.model.NewsResponse
import com.example.newsappmvvm.domain.usecases.news.*
import com.example.newsappmvvm.utils.Resource
import com.example.newsappmvvm.utils.Utils
import kotlinx.coroutines.launch

class NewsViewModel(
    val app: Application,
    private val getBreakingNewsUseCase: GetBreakingNewsUseCase,
    private val requestSearchNewsUseCase: RequestSearchNewsUseCase,
    private val getSavedArticlesUseCase: GetSavedArticlesUseCase,
    private val requestDeleteArticleUseCase: RequestDeleteArticleUseCase,
    private val requestUpsertArticleUseCase: RequestUpsertArticleUseCase
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    var searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        if (Utils.hasInternetConnection(app)) {
            val response = getBreakingNewsUseCase(countryCode, breakingNewsPage)
            when (response) {
                is Resource.Success -> breakingNews.postValue(handleBreakingNewsResponse(response))
                else -> breakingNews.postValue(response)
            }
        } else {
            breakingNews.postValue(Resource.Error("No internet connection"))
        }
    }

    fun searchNews(query: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        if (Utils.hasInternetConnection(app)) {
            val response = requestSearchNewsUseCase(query, searchNewsPage)
            when (response) {
                is Resource.Success -> searchNews.postValue(handleSearchNewsResponse(response))
                else -> searchNews.postValue(response)
            }
        } else {
            searchNews.postValue(Resource.Error("No internet connection"))
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        requestUpsertArticleUseCase(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        requestDeleteArticleUseCase(article)
    }

    fun getSavedArticles(): LiveData<List<Article>> = getSavedArticlesUseCase()

    private fun handleSearchNewsResponse(response: Resource<NewsResponse>): Resource<NewsResponse> {
        response.data?.let { resultResponse ->
            searchNewsPage++
            if (searchNewsResponse == null) {
                searchNewsResponse = resultResponse
            } else {
                val oldArticles = searchNewsResponse?.articles
                val newArticles = resultResponse.articles
                oldArticles?.addAll(newArticles)
            }
            return Resource.Success(searchNewsResponse ?: resultResponse)
        }
        return response
    }

    private fun handleBreakingNewsResponse(response: Resource<NewsResponse>): Resource<NewsResponse> {
        response.data?.let { resultResponse ->
            breakingNewsPage++
            if (breakingNewsResponse == null) {
                breakingNewsResponse = resultResponse
            } else {
                val oldArticles = breakingNewsResponse?.articles
                val newArticles = resultResponse.articles
                oldArticles?.addAll(newArticles)
            }
            return Resource.Success(breakingNewsResponse ?: resultResponse)
        }
        return response
    }
}