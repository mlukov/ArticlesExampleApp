package com.mlukov.articles.di.modules

import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mlukov.articles.ArticlesApp
import com.mlukov.articles.di.annotations.AppContext

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Singleton
    @AppContext
    internal fun providesAppContext(articlesApp : ArticlesApp) : Context {

        return articlesApp
    }

    @Provides
    @Singleton
    internal fun providesGson() : Gson {
        return GsonBuilder().create()
    }

}
