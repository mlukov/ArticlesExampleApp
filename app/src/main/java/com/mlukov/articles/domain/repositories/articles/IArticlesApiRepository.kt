package com.mlukov.articles.domain.repositories.articles

import com.mlukov.articles.api.model.ArticleDetailApiData
import com.mlukov.articles.api.model.ArticleDetailItemApiData
import com.mlukov.articles.api.model.ArticleListApi
import io.reactivex.Single

interface IArticlesApiRepository {

    // callback executed on main thread
    fun getArticleList(): Single<ArticleListApi>

    // callback executed on main thread
    fun getArticleDetails(articleId: Int): Single<ArticleDetailApiData>
}
