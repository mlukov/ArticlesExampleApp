package com.mlukov.articles.presentation.articles.list.view

import com.mlukov.articles.presentation.articles.list.model.ArticleListViewModel


interface IArticlesListView {

    fun onLoadingStateChange(isLoading : Boolean );

    fun onArticlesLoaded( articleListViewModel : ArticleListViewModel )

    fun onError( errorMessage: String)
}
