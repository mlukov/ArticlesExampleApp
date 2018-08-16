package com.mlukov.articles.domain.interactors

import com.mlukov.articles.api.model.ArticleApiData
import com.mlukov.articles.api.model.ArticleDetailApiData
import com.mlukov.articles.api.model.ArticleDetailItemApiData
import com.mlukov.articles.api.model.ArticleListApi
import com.mlukov.articles.domain.models.ArticleData
import com.mlukov.articles.domain.models.ArticleDataList
import com.mlukov.articles.domain.models.ArticleDetailData
import com.mlukov.articles.domain.repositories.articles.IArticlesApiRepository
import com.mlukov.articles.domain.repositories.local.ILocalStorageRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import java.util.ArrayList
import javax.inject.Inject

open class ArticleInteractor
@Inject constructor(val articlesApiRepository : IArticlesApiRepository,
                    val localStorageRepository : ILocalStorageRepository) : IArticleInteractor{


    override fun getArticleList() : Single<ArticleDataList> {

        return loadArticleList().first( ArticleDataList.empty())
    }

    override fun getArticleDetails(articleId : Int) : Single<ArticleDetailData> {

        return articlesApiRepository.getArticleDetails( articleId ).map{ createDetailFrom( it ) }
    }

    override fun clearCache() : Completable {
        return Completable.create( {
            if( !it.isDisposed){

                localStorageRepository.dropArticleDataListCache()
                it.onComplete()
            }
        })
    }

    //region private methods
    private fun loadArticleList() : Flowable<ArticleDataList> {

        return localStorageRepository.getArticleDataListFromCache()
                .concatWith(getArticleListFromServer())
                .filter( Predicate<ArticleDataList> { articleDataList->
                    (articleDataList.articles?.isEmpty() ?: true == false )
                })
    }

    private fun getArticleListFromServer() : Single<ArticleDataList> {

        return articlesApiRepository.getArticleList()
                .flatMap(object : Function<ArticleListApi, SingleSource<ArticleDataList>> {
                    @Throws(Exception::class)
                    override fun apply( articleApiDataList : ArticleListApi) : SingleSource<ArticleDataList> {

                        val list = ArrayList<ArticleData>()

                        for (articleApiData in articleApiDataList.items ?: emptyList()) {

                            list.add(createFrom(articleApiData))
                        }
                        val articleDataList = ArticleDataList()
                        articleDataList.articles = list

                        return localStorageRepository.addArticleDataListToCache(articleDataList)
                    }
                })
    }


    private fun createFrom( articleApiData : ArticleApiData ) : ArticleData{

        val articleData = ArticleData()
        copyData( articleData, articleApiData )

        return articleData
    }

    private fun copyData( destination : ArticleData, source : ArticleApiData? ) {

        destination.id          = source?.id
        destination.title       = source?.title
        destination.subtitle    = source?.subtitle
        destination.date        = source?.date
    }

    private fun createDetailFrom(articleApiDetailData : ArticleDetailApiData) : ArticleDetailData{

        val articleDetailData = ArticleDetailData()
        copyData( articleDetailData, articleApiDetailData.item )
        articleDetailData.body  = articleApiDetailData.item?.body

        return articleDetailData
    }
}