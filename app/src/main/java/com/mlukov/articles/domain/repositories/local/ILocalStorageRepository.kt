package com.mlukov.articles.domain.repositories.local

import com.mlukov.articles.domain.models.ArticleData
import com.mlukov.articles.domain.models.ArticleDataList
import io.reactivex.Single

interface ILocalStorageRepository {

    fun dropArticleDataListCache()
    fun getArticleDataListFromCache() : Single<ArticleDataList>
    fun addArticleDataListToCache( articleDataList : ArticleDataList) : Single<ArticleDataList>
}