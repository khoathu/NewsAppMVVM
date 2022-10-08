package com.example.newsappmvvm.domain.model

import com.example.newsappmvvm.data.dto.ArticleDto
import com.example.newsappmvvm.data.dto.Mapable
import com.example.newsappmvvm.data.dto.NewsResponseDto

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
) : Mapable<NewsResponseDto> {

    override fun map(): NewsResponseDto {
        return NewsResponseDto(articles = articles.map { it.map() } as MutableList<ArticleDto>,
            status = status,
            totalResults = totalResults)
    }

}
