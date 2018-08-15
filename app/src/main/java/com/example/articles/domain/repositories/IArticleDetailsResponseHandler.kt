package com.example.articles.domain.repositories

import com.example.articles.domain.models.ArticleDetailData

interface IArticleDetailsResponseHandler {

    fun onListLoaded( articleDetails: ArticleDetailData? )

    fun onError()

}