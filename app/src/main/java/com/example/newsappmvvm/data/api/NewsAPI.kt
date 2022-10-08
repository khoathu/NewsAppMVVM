package com.example.newsappmvvm.data.api

import com.example.newsappmvvm.data.dto.NewsResponseDto
import com.example.newsappmvvm.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        country : String = "us",
        @Query("page")
        pageNumber : Int,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponseDto>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery : String,
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = API_KEY
    ): Response<NewsResponseDto>
}