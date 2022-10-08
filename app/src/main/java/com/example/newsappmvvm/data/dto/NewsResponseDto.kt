package com.example.newsappmvvm.data.dto

import com.example.newsappmvvm.domain.model.Article
import com.example.newsappmvvm.domain.model.NewsResponse

data class NewsResponseDto(
    val articles: MutableList<ArticleDto>,
    val status: String,
    val totalResults: Int
) : Mapable<NewsResponse> {

    override fun map(): NewsResponse {
        return NewsResponse(articles = articles.map { item -> item.map() } as MutableList<Article>,
            status = status,
            totalResults = totalResults)
    }

}
