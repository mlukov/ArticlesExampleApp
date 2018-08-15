package com.mlukov.articles

import android.app.Application

//import com.mlukov.articles.di.DaggerApplicationComponent
import com.mlukov.articles.di.modules.DeviceModule
import com.mlukov.articles.di.modules.NetworkModule
import java.io.File

class ArticlesApp : Application() {

    override fun onCreate() {

        super.onCreate()

        val cacheFile =  File( getCacheDir(), "responses" )
//
//        DaggerApplicationComponent.builder()
//                .application( this )
//                .network( NetworkModule( cacheFile ) )
//                .device( DeviceModule( this ) )
//                .build()
//                .inject(this)
    }
}
