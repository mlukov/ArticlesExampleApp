package com.example.articles.domain.repositories

import io.reactivex.disposables.Disposable

interface IArticlesApiRepo : IRepo {

    // callback executed on main thread
    fun getArticleList(resultHandler: IArticleListResponseHandler?): Disposable

    // callback executed on main thread
    fun getArticleDetails(articleId: Int, resultHandler: IArticleDetailsResponseHandler?): Disposable
}
