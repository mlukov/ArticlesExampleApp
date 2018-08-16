package com.mlukov.articles.presentation.articles.details.presenter

import com.mlukov.articles.mvp.IBasePresenter

interface IArticleDetailsPresenter: IBasePresenter {

    fun loadArticleDetails( articleId:Int )
}