package com.mlukov.articles.api

import com.mlukov.articles.api.model.ArticleDetailApiData
import com.mlukov.articles.api.model.ArticleDetailItemApiData
import com.mlukov.articles.api.model.ArticleListApi
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Path

interface ArticlesApiController {

    @GET("contentList.json")
    fun getArticleList(): Single<ArticleListApi>

    @GET("content/{article_id}.json")
    fun getArticleDetails(@Path(value = "article_id", encoded = true) articleId: Int): Single<ArticleDetailApiData>
}
