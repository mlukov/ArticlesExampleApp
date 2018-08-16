package com.mlukov.articles.di.modules

import com.mlukov.articles.di.annotations.ActivityScope
import com.mlukov.articles.presentation.main.IMainNavigator
import com.mlukov.articles.presentation.main.MainActivity
import dagger.Binds
import dagger.Module

@Module
interface ActivityModule {

    @Binds
    @ActivityScope
    fun bindsMainNavigator(mainActivity: MainActivity): IMainNavigator

}