package com.mlukov.articles.domain.repositories.articles


import com.mlukov.articles.api.ArticlesApiController
import com.mlukov.articles.api.model.ArticleApiData
import com.mlukov.articles.api.model.ArticleDetailApiData

import javax.inject.Inject
import io.reactivex.Single


class ArticlesApiRepository
@Inject
constructor(internal var apiController : ArticlesApiController) : IArticlesApiRepository {

    override fun getArticleList() : Single<List<ArticleApiData>> {

        return apiController.getArticleList()
    }

    override fun getArticleDetails(articleId : Int) : Single<ArticleDetailApiData> {

        return apiController.getArticleDetails(articleId)
    }
}