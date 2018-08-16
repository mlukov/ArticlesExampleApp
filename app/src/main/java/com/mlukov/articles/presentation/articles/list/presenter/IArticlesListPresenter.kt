package com.mlukov.articles.presentation.articles.list.presenter

import com.mlukov.articles.mvp.IBasePresenter

interface IArticlesListPresenter :IBasePresenter {

    fun loadArticles( refresh: Boolean )
}
