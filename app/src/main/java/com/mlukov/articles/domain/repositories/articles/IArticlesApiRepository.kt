package com.mlukov.articles.domain.repositories.articles

import com.mlukov.articles.api.model.ArticleApiData
import com.mlukov.articles.api.model.ArticleDetailApiData
import io.reactivex.Single

interface IArticlesApiRepository {

    // callback executed on main thread
    fun getArticleList(): Single<List<ArticleApiData>>

    // callback executed on main thread
    fun getArticleDetails(articleId: Int): Single<ArticleDetailApiData>
}
