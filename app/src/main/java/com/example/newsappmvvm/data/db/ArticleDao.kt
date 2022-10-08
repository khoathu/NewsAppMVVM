package com.example.newsappmvvm.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsappmvvm.data.dto.ArticleDto

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: ArticleDto) : Long

    @Query("SELECT * FROM articles")
    fun getAllArticles() : LiveData<List<ArticleDto>>

    @Delete
    suspend fun deleteArticle(article: ArticleDto)
}