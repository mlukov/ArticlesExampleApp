package com.mlukov.articles.presentation.articles.list.presenter

import android.util.Log
import com.mlukov.articles.R
import com.mlukov.articles.domain.interactors.IArticleInteractor
import com.mlukov.articles.domain.models.ArticleData
import com.mlukov.articles.mvp.BasePresenter
import com.mlukov.articles.presentation.articles.list.model.ArticleListViewModel
import com.mlukov.articles.presentation.articles.list.model.ArticleViewData
import com.mlukov.articles.presentation.providers.INetworkInfoProvider
import com.mlukov.articles.presentation.providers.IResourceProvider
import com.mlukov.articles.presentation.articles.list.view.IArticlesListView
import com.mlukov.articles.utils.ISchedulersProvider


import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ArticlesPresenter @Inject
constructor(private val articlesView : IArticlesListView,
            private val articleInteractor : IArticleInteractor,
            private val schedulersProvider : ISchedulersProvider,
            private val resourceProvider : IResourceProvider,
            private val networkInfoProvider : INetworkInfoProvider) : BasePresenter(), IArticlesPresenter {


    override fun loadArticles(  refresh: Boolean ) {

        if( refresh ){

            add( articleInteractor.clearCache()
                    .subscribeOn( schedulersProvider.ioScheduler())
                    .observeOn( schedulersProvider.uiScheduler())
                    .subscribe( {
                        add( getArticleList())
                    },
                            {
                                onError( throwable = it)
                            }) )
        }
        else
            add( getArticleList() )
    }

    private fun getArticleList() : Disposable{

        return articleInteractor.getArticles().map {

                val articlesList = ArticleListViewModel()

                if( it.articles == null  )
                    return@map articlesList

                for( article in it.articles!!){

                    articlesList.articles.add( createFrom( article ))
                }
                return@map articlesList
            }
                .subscribeOn( schedulersProvider.ioScheduler())
                .observeOn( schedulersProvider.uiScheduler())
                .subscribe({

                    articlesView.onArticlesLoaded( it)
                    articlesView.loading( false )
                },
                        {

                            onError( throwable = it )
                        })

    }

    private fun onError( throwable : Throwable ){

        Log.e( TAG, throwable.message, throwable)

        articlesView.loading( false )

        var resId =
                if( !networkInfoProvider.isNetworkConnected )
                    R.string.internet_connection_offline
                else
                    R.string.oops_went_wrong_try

        articlesView.onError( resourceProvider.getString( resId ) )
    }

    private fun createFrom( articleData : ArticleData ) : ArticleViewData{

        return ArticleViewData( articleData.id,
                articleData.title,
                articleData.subtitle,
                articleData.date)

    }

    companion object {

        private val TAG = ArticlesPresenter::class.simpleName
    }
}
