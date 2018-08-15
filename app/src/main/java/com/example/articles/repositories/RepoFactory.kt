package com.example.articles.repositories

import android.content.Context

import com.example.articles.domain.repositories.IRepo
import com.example.articles.domain.repositories.IRepoFactory
import com.example.articles.domain.repositories.IArticlesApiRepo
import com.example.articles.domain.repositories.IDeviceRepo
import com.example.articles.domain.repositories.IResourceRepo

class RepoFactory(private val context: Context) : IRepoFactory {

    override fun <T : IRepo> getRepository(classOfT: Class<T>): T {

        var repository: T? = null

        if (IArticlesApiRepo::class == classOfT)
            repository = ArticlesApiRepo() as T
        if (IDeviceRepo::class == classOfT)
            repository = DeviceRepo(context) as T
        else if (IResourceRepo::class == classOfT)
            repository = ResourceRepo(context) as T

        return repository!!
    }
}
