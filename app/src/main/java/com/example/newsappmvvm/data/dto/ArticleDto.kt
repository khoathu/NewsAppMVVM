package com.example.newsappmvvm.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsappmvvm.domain.model.Article

@Entity(
    tableName = "articles"
)
data class ArticleDto(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: SourceDto?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val isFavorite: Boolean = false
) : Mapable<Article> {

    //map to domain model
    override fun map(): Article {
        return Article(
            id = id,
            author = author,
            content = content,
            description = description,
            publishedAt = publishedAt,
            source = source?.map(),
            title = title,
            url = url,
            urlToImage = urlToImage,
            isFavorite = isFavorite
        )
    }
}
