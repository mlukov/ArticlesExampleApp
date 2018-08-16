package com.mlukov.articles.di.modules

import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mlukov.articles.ArticlesApp
import com.mlukov.articles.di.annotations.AppContext
import com.mlukov.articles.domain.interactors.ArticleInteractor
import com.mlukov.articles.domain.interactors.IArticleInteractor
import com.mlukov.articles.domain.repositories.articles.ArticlesApiRepository
import com.mlukov.articles.domain.repositories.articles.IArticlesApiRepository
import com.mlukov.articles.domain.repositories.local.ILocalStorageRepository
import com.mlukov.articles.domain.repositories.local.LocalStorageRepository
import com.mlukov.articles.utils.ISchedulersProvider
import com.mlukov.articles.utils.SchedulersProvider

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    @Singleton
    @AppContext
    internal fun providesAppContext(articlesApp : ArticlesApp) : Context {

        return articlesApp
    }

    @Provides
    @Singleton
    internal fun providesGson() : Gson {
        return GsonBuilder()
                .setDateFormat("dd/mm/yyyy HH:mm")
                .create()
    }

    @Provides
    internal fun providesLocalStorageRepository(@AppContext context: Context, gson: Gson): ILocalStorageRepository {

        return LocalStorageRepository(context.cacheDir, gson)
    }

    @Provides
    internal fun providesSchedulersProvider(schedulersProvider: SchedulersProvider): ISchedulersProvider {

        return schedulersProvider
    }

    @Provides
    internal fun providesArticlesApiRepository( articlesApiRepository: ArticlesApiRepository): IArticlesApiRepository {

        return articlesApiRepository
    }

    @Provides
    fun providesArticleInteractor(articleInteractor: ArticleInteractor): IArticleInteractor {

        return articleInteractor
    }

}
