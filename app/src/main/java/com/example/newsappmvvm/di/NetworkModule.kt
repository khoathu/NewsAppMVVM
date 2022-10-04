package com.example.newsappmvvm.di

import com.example.newsappmvvm.BuildConfig
import com.example.newsappmvvm.api.NewsAPI
import com.example.newsappmvvm.utils.Constants.Companion.BASE_URL
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = DI.Module(name = "network_module")
{
    bind<OkHttpClient>() with singleton { provideOkHttpClient() }
    bind<Gson>() with singleton { provideGson() }
    bind<Retrofit>() with singleton {
        provideRetrofit(
            gson = instance(),
            okHttpClient = instance()
        )
    }
    bind<NewsAPI>() with singleton { provideApiService(retrofit = instance()) }
}

fun provideOkHttpClient(): OkHttpClient {
    val httpBuilder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpBuilder.addInterceptor(loggingInterceptor)
    }
    return httpBuilder.build()
}

fun provideGson(): Gson {
    return Gson()
}

fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun provideApiService(retrofit: Retrofit): NewsAPI {
    return retrofit.create(NewsAPI::class.java)
}