package com.example.articles.repositories

import com.example.articles.domain.models.ArticleData
import com.example.articles.domain.models.ArticleDetailData

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticlesApiController {

    @get:GET("/contentList.json")
    val articleList: Call<List<ArticleData>>

    @GET("/{article_id}.json")
    fun getArticleDetails(@Path(value = "article_id", encoded = true) articleId: Int): Call<ArticleDetailData>
}
