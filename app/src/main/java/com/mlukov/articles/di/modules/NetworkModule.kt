package com.mlukov.articles.di.modules

import android.util.Log
import com.google.gson.Gson
import com.mlukov.articles.BuildConfig
import com.mlukov.articles.api.ArticlesApiController
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule( private val cacheFile: File ) {

    companion object {
        val TAG = NetworkModule::class.java.simpleName
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient{

        var cache: Cache? = null

        try {

            cache = Cache( cacheFile, 10* 1024*1024 )
        }
        catch ( ex : Exception ){

            Log.e( TAG, ex.message, ex )
        }

        return OkHttpClient.Builder()
                .connectTimeout(2L, TimeUnit.MINUTES)
                .readTimeout(2L, TimeUnit.MINUTES)
                .writeTimeout(2L, TimeUnit.MINUTES)
                .cache( cache)
                .build()
    }


    @Provides
    @Singleton
    fun providesArticlesApiController( okHttpClient : OkHttpClient, gson : Gson ): ArticlesApiController{

        val retrofit = Retrofit.Builder()
                .baseUrl( BuildConfig.API_ENDPOINT)
                .client( okHttpClient )
                .addConverterFactory( GsonConverterFactory.create( gson ) )
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
                .build()

        return retrofit.create( ArticlesApiController::class.java )
    }

}