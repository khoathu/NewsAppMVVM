package com.example.newsappmvvm.utils

import com.example.newsappmvvm.BuildConfig

class Constants {
    companion object
    {
        const val API_KEY = BuildConfig.API_KEY
        const val BASE_URL = "https://newsapi.org"
        const val SEARCH_NEWS_TIME_DELAY = 700L
        const val QUERY_PAGE_SIZE = 20

    }
}