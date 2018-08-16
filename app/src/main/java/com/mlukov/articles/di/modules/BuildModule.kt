package com.mlukov.articles.di.modules

import com.mlukov.articles.di.annotations.ActivityScope
import com.mlukov.articles.presentation.articles.details.di.ArticleDetailsFragmentModule
import com.mlukov.articles.presentation.articles.list.di.ArticlesListFragmentModule
import com.mlukov.articles.presentation.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BuildModule {

    @ContributesAndroidInjector(modules = arrayOf(ActivityModule::class, ArticlesListFragmentModule::class, ArticleDetailsFragmentModule::class))
    @ActivityScope
    fun bindsMainActivity(): MainActivity
}