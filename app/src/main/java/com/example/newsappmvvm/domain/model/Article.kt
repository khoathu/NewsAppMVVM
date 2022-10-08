package com.example.newsappmvvm.domain.model

import com.example.newsappmvvm.data.dto.ArticleDto
import java.io.Serializable

data class Article(
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Mapable<ArticleDto>, Serializable {

    override fun hashCode(): Int {
        var result = id.hashCode()
        if (url.isNullOrEmpty()) {
            result = 31 * result + url.hashCode()
        }
        return result
    }

    //map to data entity
    override fun map(): ArticleDto {
        return ArticleDto(
            id = id,
            author = author,
            content = content,
            description = description,
            publishedAt = publishedAt,
            source = source?.map(),
            title = title,
            url = url,
            urlToImage = urlToImage
        )
    }
}
