package com.example.newsappmvvm.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsappmvvm.data.dto.ArticleDto

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: ArticleDto): Long

    @Delete
    suspend fun deleteArticle(article: ArticleDto)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<ArticleDto>>

    @Query("SELECT * FROM articles WHERE isFavorite = 1")
    fun getFavoriteArticles(): LiveData<List<ArticleDto>>

    @Query("SELECT * FROM articles WHERE isFavorite = 0")
    fun getLocalBreakingNews(): List<ArticleDto>
}