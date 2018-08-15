package com.mlukov.articles.presentation.articles.list.view

import com.mlukov.articles.presentation.articles.list.model.ArticleListViewModel
import com.mlukov.articles.presentation.articles.list.presenter.IArticlesPresenter


interface IArticlesListView {

    fun loading( isLoading : Boolean );

    fun onArticlesLoaded( articleListViewModel : ArticleListViewModel )

    fun onError( errorMessage: String)
}
