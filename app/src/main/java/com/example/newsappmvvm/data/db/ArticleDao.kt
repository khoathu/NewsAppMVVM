package com.example.newsappmvvm.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsappmvvm.data.dto.ArticleDto
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsert(article: ArticleDto): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveArticles(articles: List<ArticleDto>)

    @Delete
    abstract suspend fun deleteArticle(article: ArticleDto)

    @Query("DELETE FROM articles WHERE isFavorite == 0")
    abstract suspend fun deleteAllBreakingArticles()

    @Query("DELETE FROM articles")
    abstract suspend fun deleteAllArticles()

    @Query("SELECT * FROM articles")
    abstract fun getAllArticles(): Flow<List<ArticleDto>>

    @Query("SELECT * FROM articles WHERE isFavorite == 1")
    abstract fun getFavoriteArticles(): Flow<List<ArticleDto>>

    @Query("SELECT * FROM articles WHERE isFavorite == 0")
    abstract suspend fun getLocalBreakingNews(): List<ArticleDto>
}