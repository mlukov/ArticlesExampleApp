package com.example.articles.presentation.articles.list.presenter

interface IArticlesPresenter {

    fun onViewCreated()

    fun onViewShown()

    fun onViewHidden()

    fun onViewDestroyed()

    fun onRefresh()
}
