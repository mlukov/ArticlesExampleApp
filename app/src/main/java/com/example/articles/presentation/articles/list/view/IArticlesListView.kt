package com.example.articles.presentation.articles.list.view

import com.example.articles.presentation.articles.list.model.ArticleListViewModel
import com.example.articles.presentation.articles.list.presenter.IArticlesPresenter


interface IArticlesListView {

    fun onStartLoadingUsers()

    fun onUsersLoaded(usersViewModel: ArticleListViewModel)

    fun onUserLoadError(errorMessage: String)

    fun setPresenter(usersPresenter: IArticlesPresenter)

}
