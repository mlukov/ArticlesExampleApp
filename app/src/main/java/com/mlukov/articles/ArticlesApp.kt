package com.mlukov.articles

import android.app.Activity
import android.app.Application
import com.mlukov.articles.di.components.DaggerApplicationComponent

//import com.mlukov.articles.di.DaggerApplicationComponent
import com.mlukov.articles.di.modules.DeviceModule
import com.mlukov.articles.di.modules.NetworkModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import java.io.File
import javax.inject.Inject

class ArticlesApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {

        super.onCreate()

        val cacheFile =  File( getCacheDir(), "responses" )

        DaggerApplicationComponent.builder()
                .application( this )
                .network( NetworkModule( cacheFile ) )
                .device( DeviceModule( this ) )
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
       return dispatchingAndroidInjector
    }
}
