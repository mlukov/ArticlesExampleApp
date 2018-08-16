package com.mlukov.articles.presentation.main

import com.mlukov.articles.presentation.articles.details.model.ArticleDetailsViewData

interface IMainNavigator {

        fun showArticleList()

        fun showArticleDetails( articleDetailsViewData: ArticleDetailsViewData )
}