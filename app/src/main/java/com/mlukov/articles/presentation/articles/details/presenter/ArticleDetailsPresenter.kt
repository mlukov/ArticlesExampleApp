package com.mlukov.articles.presentation.articles.details.presenter

import android.util.Log
import com.mlukov.articles.R
import com.mlukov.articles.domain.interactors.IArticleInteractor
import com.mlukov.articles.domain.models.ArticleData
import com.mlukov.articles.domain.models.ArticleDetailData
import com.mlukov.articles.mvp.BasePresenter
import com.mlukov.articles.presentation.articles.details.model.ArticleDetailsViewData
import com.mlukov.articles.presentation.articles.details.view.IArticleDetailsView
import com.mlukov.articles.presentation.articles.list.model.ArticleListViewModel
import com.mlukov.articles.presentation.articles.list.model.ArticleViewData
import com.mlukov.articles.presentation.providers.INetworkInfoProvider
import com.mlukov.articles.presentation.providers.IResourceProvider
import com.mlukov.articles.presentation.articles.list.view.IArticlesListView
import com.mlukov.articles.utils.ISchedulersProvider


import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ArticleDetailsPresenter @Inject
constructor(val articleDetailsView : IArticleDetailsView,
            val articleInteractor : IArticleInteractor,
            val schedulersProvider : ISchedulersProvider,
            val resourceProvider : IResourceProvider,
            val networkInfoProvider : INetworkInfoProvider) : BasePresenter(), IArticleDetailsPresenter{


    override fun loadArticleDetails( articleId:Int) {

        add( getArticleDetails(articleId) )
    }

    private fun getArticleDetails( articleId:Int) : Disposable{

        return articleInteractor.getArticleDetails( articleId ).map {

                return@map createFrom( it )

            }
                .subscribeOn( schedulersProvider.ioScheduler())
                .observeOn( schedulersProvider.uiScheduler())
                .subscribe({

                    articleDetailsView.onDetailsLoaded( it )
                    articleDetailsView.onLoadingStateChange( false )
                },
                        {

                            onError( throwable = it )
                        })

    }

    private fun onError( throwable : Throwable ){

        Log.e( TAG, throwable.message, throwable)

        articleDetailsView.onLoadingStateChange( false )

        var resId =
                if( !networkInfoProvider.isNetworkConnected )
                    R.string.internet_connection_offline
                else
                    R.string.oops_went_wrong_try

        articleDetailsView.onError( resourceProvider.getString( resId ) )
    }

    private fun createFrom( articleData : ArticleDetailData) : ArticleDetailsViewData{

        return ArticleDetailsViewData( articleData.id,
                articleData.title,
                articleData.subtitle,
                articleData.date,
                articleData.body)
    }

    companion object {

        private val TAG = ArticleDetailsPresenter::class.simpleName
    }
}
