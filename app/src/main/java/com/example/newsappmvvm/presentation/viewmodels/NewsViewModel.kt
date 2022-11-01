package com.example.newsappmvvm.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.model.NewsResponse
import com.example.newsappmvvm.domain.usecases.news.*
import com.example.newsappmvvm.utils.Resource
import com.example.newsappmvvm.utils.Utils
import com.example.newsappmvvm.utils.Utils.Companion.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    val app: Application,
    private val getBreakingNewsUseCase: GetBreakingNewsUseCase,
    private val requestSearchNewsUseCase: RequestSearchNewsUseCase,
    private val getSavedArticlesUseCase: GetSavedArticlesUseCase,
    private val requestDeleteArticleUseCase: RequestDeleteArticleUseCase,
    private val requestUpsertArticleUseCase: RequestUpsertArticleUseCase,
    private val requestDeleteAllArticlesUseCase: RequestDeleteAllArticlesUseCase
) : AndroidViewModel(app) {

    //val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    //var breakingNewsPage = 1
    //var breakingNewsResponse: NewsResponse? = null

    //var searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    //var searchNewsPage = 1
    //var searchNewsResponse: NewsResponse? = null

    private val _breakingNewsState = MutableStateFlow(BreakingNewsState(isLoading = false))
    val breakingNewsState: StateFlow<BreakingNewsState> = _breakingNewsState.asStateFlow()

    private val _searchNewsState = MutableStateFlow(SearchNewsState(isLoading = false))
    val searchNewsState: StateFlow<SearchNewsState> = _searchNewsState.asStateFlow()

    init {
        getBreakingNews("us")
        //test
        /*viewModelScope.launch {
            requestDeleteAllArticlesUseCase()
        }*/
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        getBreakingNewsUseCase(
            countryCode,
            breakingNewsState.value.breakingNewsPage
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        handleBreakingNewsResponse(it)
                    }
                }
                is Resource.Error -> {
                    _breakingNewsState.value = breakingNewsState.value.copy(
                        errorMessage = result.message.toString(),
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _breakingNewsState.value = breakingNewsState.value.copy(isLoading = true)
                }
            }
        }.launchIn(this)
    }

    private fun handleBreakingNewsResponse(response: NewsResponse) {
        val articles = response.let { resultResponse ->
            _breakingNewsState.value.breakingNewsTotalResults = resultResponse.totalResults
            _breakingNewsState.value.breakingNewsPage++

            val currentArticles = breakingNewsState.value.breakingNewsArticles
            if (!currentArticles.isNullOrEmpty()) {
                val newArticles = resultResponse.articles
                currentArticles.addAll(newArticles)
            }

            currentArticles ?: resultResponse.articles
        }
        _breakingNewsState.value = breakingNewsState.value.copy(
            breakingNewsArticles = articles, isLoading = false
        )
    }

    fun resetBreakingNewPage() {
        breakingNewsState.value.breakingNewsPage = 1
    }

    fun searchNews(query: String) = viewModelScope.launch {
        if (hasInternetConnection(app)) {
            _searchNewsState.value.newSearchQuery = query
            if (_searchNewsState.value.newSearchQuery != _searchNewsState.value.oldSearchQuery) {
                _searchNewsState.value.searchNewsPage = 1
                _searchNewsState.value.searchNewsArticles = null
            }
            requestSearchNewsUseCase(query, searchNewsState.value.searchNewsPage).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { newsResponse ->
                            handleSearchNewsResponse(newsResponse)
                        }
                    }
                    is Resource.Error -> {
                        _searchNewsState.value = searchNewsState.value.copy(
                            errorMessage = result.message.toString(),
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _searchNewsState.value = searchNewsState.value.copy(isLoading = true)
                    }
                }
            }.launchIn(this)
        } else {
            _searchNewsState.value = searchNewsState.value.copy(
                errorMessage = "No internet connection",
                isLoading = false
            )
        }
    }

    private fun handleSearchNewsResponse(response: NewsResponse) {

        val articles = response.let { resultResponse ->
            _searchNewsState.value.searchNewsTotalResults = resultResponse.totalResults

            val currentArticles = searchNewsState.value.searchNewsArticles
            if (currentArticles == null || searchNewsState.value.newSearchQuery != searchNewsState.value.oldSearchQuery) {
                _searchNewsState.value.oldSearchQuery = searchNewsState.value.newSearchQuery
                resultResponse.articles
            } else {
                val newArticles = resultResponse.articles
                currentArticles.addAll(newArticles)
                currentArticles
            }
        }
        _searchNewsState.value.searchNewsPage++
        _searchNewsState.value = searchNewsState.value.copy(
            searchNewsArticles = articles, isLoading = false
        )
    }

    fun resetSearchPage() {
        searchNewsState.value.searchNewsArticles = null
        searchNewsState.value.searchNewsPage = 1
        searchNewsState.value.newSearchQuery = null
        searchNewsState.value.oldSearchQuery = null
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        requestUpsertArticleUseCase(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        requestDeleteArticleUseCase(article)
    }

    fun getFavoriteArticles(): Flow<List<Article>> = getSavedArticlesUseCase()

    /*
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    fun searchNew2(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    suspend fun safeSearchNewsCall(searchQuery: String) = viewModelScope.launch {
        newSearchQuery = searchQuery
        if (newSearchQuery != oldSearchQuery) {
            searchNewsPage = 1
            searchNewsResponse = null
        }
        try {
            if (hasInternetConnection(app)) {
                requestSearchNewsUseCase(searchQuery, searchNewsPage).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { newsResponse ->
                                searchNews.postValue(handleSearchNewsResponse2(newsResponse))
                                //handleSearchNewsResponse(newsResponse)
                            }
                        }
                        is Resource.Error -> {
                            searchNews.postValue(Resource.Error(result.message.toString()))
                        }
                        is Resource.Loading -> {
                            searchNews.postValue(Resource.Loading())
                        }
                    }
                }.launchIn(this)
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleSearchNewsResponse2(response: NewsResponse): Resource<NewsResponse> {
        response.let { resultResponse ->
            if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                //searchNewsPage = 1
                oldSearchQuery = newSearchQuery
                searchNewsResponse = resultResponse
            } else {
                val oldArticles = searchNewsResponse?.articles
                val newArticles = resultResponse.articles
                oldArticles?.addAll(newArticles)
            }
            searchNewsPage++
            return Resource.Success(searchNewsResponse ?: resultResponse)
        }
    }
    */

    data class BreakingNewsState(

        val isLoading: Boolean = false,

        //var breakingNewsResponse: NewsResponse? = null,
        var breakingNewsPage: Int = 1,
        var breakingNewsArticles: MutableList<Article>? = null,
        var breakingNewsTotalResults: Int = 0,

        //val searchNewsResponse: NewsResponse? = null,
        //var searchNewsPage: Int = 1,
        //var searchNewsArticles: MutableList<Article>? = null,
        //var searchNewsTotalResults: Int = 0,

        val errorMessage: String = ""
    )

    data class SearchNewsState(
        val isLoading: Boolean = false,
        var searchNewsPage: Int = 1,
        var searchNewsArticles: MutableList<Article>? = null,
        var searchNewsTotalResults: Int = 0,
        var newSearchQuery: String? = null,
        var oldSearchQuery: String? = null,

        val errorMessage: String = ""
    )
}