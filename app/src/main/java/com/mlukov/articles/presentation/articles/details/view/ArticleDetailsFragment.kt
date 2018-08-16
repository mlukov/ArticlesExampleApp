package com.mlukov.articles.presentation.articles.details.view


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.mlukov.articles.R
import com.mlukov.articles.presentation.articles.details.model.ArticleDetailsViewData
import com.mlukov.articles.presentation.articles.details.presenter.IArticleDetailsPresenter
import com.mlukov.articles.presentation.articles.list.model.ArticleListViewModel
import com.mlukov.articles.presentation.articles.list.model.ArticleViewData
import com.mlukov.articles.utils.toSimpleDayTimeString
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlin.jvm.java

class ArticleDetailsFragment : Fragment(), IArticleDetailsView {

    companion object {

        val TAG = ArticleDetailsFragment::class.java.simpleName

        val ARTICLE_DETAILS_KEY = "ArticleDetailsFragment.articleDetailsViewData"

        fun newInstance( articleDetailsViewData: ArticleDetailsViewData): ArticleDetailsFragment{

            val bundle = Bundle()
            bundle.putSerializable(ARTICLE_DETAILS_KEY, articleDetailsViewData)
            val fragment =  ArticleDetailsFragment()
            fragment.setArguments(bundle)
            return fragment;
        }
    }

    private var listSwipeRefreshLayout: SwipeRefreshLayout? = null

    private var titleTextView: AppCompatTextView? = null
    private var subtitleTextView: AppCompatTextView? = null
    private var dateTextView: AppCompatTextView? = null
    private var bodyTextView: AppCompatTextView? = null

    private var articleDetailsViewData : ArticleDetailsViewData? = null

    @Inject
    public lateinit var detailsPresenter: IArticleDetailsPresenter


    //region constructor
    fun ArticleDetailsFragment() {
        // Required empty public constructor
    }
    //endregion

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = savedInstanceState ?: getArguments()
        articleDetailsViewData = arguments?.getSerializable( ARTICLE_DETAILS_KEY ) as ArticleDetailsViewData
    }

    override fun onAttach(context: Context) {

        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {

        val view = inflater.inflate(R.layout.fragment_article_details, container, false)

        listSwipeRefreshLayout  = view.findViewById( R.id.swipeToRefreshLayout )
        titleTextView           = view.findViewById( R.id.titleTextView )
        subtitleTextView        = view.findViewById( R.id.subtitleTextView )
        dateTextView            = view.findViewById( R.id.dateTextView )
        bodyTextView            = view.findViewById( R.id.bodyTextView )

        listSwipeRefreshLayout?.setColorSchemeResources(android.R.color.holo_orange_dark)
        listSwipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            detailsPresenter.loadArticleDetails(articleDetailsViewData?.id ?: 0 )
        })

        return view
    }

    override fun onResume() {

        super.onResume()
        setData()

        detailsPresenter.loadArticleDetails( articleDetailsViewData?.id ?: 0 )

    }

    override fun onPause() {

        super.onPause()
        detailsPresenter.dispose()
    }

    override fun onLoadingStateChange(isLoading: Boolean) {

        listSwipeRefreshLayout?.setRefreshing(isLoading)
    }

    override fun onDetailsLoaded(articleDetailsViewData: ArticleDetailsViewData) {

        this.articleDetailsViewData = articleDetailsViewData
        setData()
    }

    override fun onError(errorMessage: String) {

        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun setData(){

        titleTextView   ?.text = articleDetailsViewData?.title               ?: ""
        subtitleTextView?.text = articleDetailsViewData?.subtitle            ?: ""
        dateTextView    ?.text = articleDetailsViewData?.date?.toSimpleDayTimeString() ?: ""
        bodyTextView    ?.text = articleDetailsViewData?.body                ?: ""
    }

    //endregion IArticlesListView implementation

}
