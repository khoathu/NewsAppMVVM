package com.example.newsappmvvm.di

import android.content.Context
import androidx.room.Room
import com.example.newsappmvvm.api.NewsAPI
import com.example.newsappmvvm.db.ArticleDatabase
import com.example.newsappmvvm.repository.NewsRepository
import com.example.newsappmvvm.utils.Constants.Companion.BASE_URL
import com.example.newsappmvvm.viewmodels.NewsViewModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val viewModelModule = module {
    viewModel {
        NewsViewModel(app = get(), newsRepository = get())
    }
}

val repositoryModule = module {
    single { NewsRepository(db = get(), api = get()) }
}

val databaseModule = module {
    fun provideDatabase(context: Context): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            "article_db.db"
        ).build()
    }
    single {
        androidContext()
    }
    single { provideDatabase(get()) }
    single { get<ArticleDatabase>().getArticleDao() }
}

val apiModule = module {
    fun provideApi(retrofit: Retrofit): NewsAPI {
        return retrofit.create(NewsAPI::class.java)
    }
    single { provideApi(retrofit = get()) }
}

val retrofitModule = module {
    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun provideLogging(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(logging)
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(factory: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(okHttpClient)
            .build()
    }

    single { provideGson() }
    single { provideLogging() }
    single { provideOkHttpClient(logging = get()) }
    single { provideRetrofit(factory = get(), okHttpClient = get()) }
}
