package com.mlukov.articles.di.modules

import android.content.Context
import com.mlukov.articles.presentation.providers.INetworkInfoProvider
import com.mlukov.articles.presentation.providers.IResourceProvider
import com.mlukov.articles.presentation.providers.NetworkInfoProvider
import com.mlukov.articles.presentation.providers.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceModule ( val context : Context ){

    @Provides
    @Singleton
    fun providesNetworkInfoProvider() :INetworkInfoProvider{

        return NetworkInfoProvider( context )
    }

    @Provides
    @Singleton
    fun providesResourceProvider() :IResourceProvider{

        return ResourceProvider( context )
    }

}