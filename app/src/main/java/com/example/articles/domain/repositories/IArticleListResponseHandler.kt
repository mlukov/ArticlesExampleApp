package com.example.articles.domain.repositories

import com.example.articles.domain.models.ArticleData

interface IArticleListResponseHandler {

    fun onListLoaded(articleDataList: List<ArticleData>?)

    fun onError()
}
