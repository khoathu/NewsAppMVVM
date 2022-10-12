package com.example.newsappmvvm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newsappmvvm.data.dto.ArticleDto

@Database(
    entities = [ArticleDto::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE articles ADD COLUMN isFavorite INTEGER DEFAULT 0 NOT NULL")
                //database.execSQL("UPDATE Articles SET isFavorite = 0")
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE UNIQUE INDEX 'index_articles_id_url' ON 'articles'('id','url')")
            }
        }
    }

    /*companion object{
        @Volatile
        private var instance: ArticleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK)
        {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()

    }*/

}