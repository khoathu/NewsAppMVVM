package com.example.newsappmvvm

import android.content.Context
import androidx.room.Room
import com.example.newsappmvvm.db.ArticleDatabase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val databaseModule = DI.Module(name = "database_module")
{
    bind<ArticleDatabase>() with singleton { provideDatabase(context = instance()) }
}

fun provideDatabase(context: Context): ArticleDatabase {
    return Room.databaseBuilder(
        context,
        ArticleDatabase::class.java,
        "article_db.db"
    ).build()
}