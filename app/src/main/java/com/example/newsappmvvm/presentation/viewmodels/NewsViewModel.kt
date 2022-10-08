package com.example.newsappmvvm.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsappmvvm.MyApplication
import com.example.newsappmvvm.data.repository.NewsRepositoryImpl
import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.model.NewsResponse
import com.example.newsappmvvm.utils.Resource
import kotlinx.coroutines.launch
import okio.IOException

class NewsViewModel(
    val app: Application,
    val newsRepository: NewsRepositoryImpl
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

        if (hasInternetConnection()) {
            val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
            when (response) {
                is Resource.Success -> {
                    breakingNews.postValue(handleBreakingNewsResponse(response))
                }
                is Resource.Error -> {
                    breakingNews.postValue(Resource.Error(response.message!!))
                }
                else -> {
                    breakingNews.postValue(Resource.Error("Conversion Error"))
                }
            }
        } else {
            breakingNews.postValue(Resource.Error("No internet connection"))
        }
    }

    fun searchNews(query: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(query, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getSavedArticles(): LiveData<List<Article>> = newsRepository.getSavedArticles()

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
        return Resource.Error(response.message.toString())
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
        return Resource.Error(response.message.toString())
    }

    /*private fun handleSearchNewsResponse(response: Response<NewsResponseDto>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse.map()
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.map().articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse.map())
            }
        }
        return Resource.Error(response.message())
    }*/

    /*private fun handleBreakingNewsResponse(response: Response<NewsResponseDto>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse.map()
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.map().articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse.map())
            }
        }
        return Resource.Error(response.message())
    }*/

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}