package com.mlukov.articles.presentation.articles.list.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.mlukov.articles.R
import com.mlukov.articles.presentation.articles.list.model.ArticleListViewModel
import com.mlukov.articles.presentation.articles.list.model.ArticleViewData
import com.mlukov.articles.presentation.articles.list.presenter.IArticlesPresenter
import javax.inject.Inject

class ArticlesFragment : Fragment(), IArticlesListView {

    companion object {

        public val TAG = ArticlesFragment::class.simpleName

        public fun newInstance(): ArticlesFragment{
            return ArticlesFragment()
        }
    }


    private var listRecyclerView: RecyclerView? = null
    private var listSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var emptyListText: TextView? = null

    private var listAdapter: ArticlesAdapter? = null
    private val dataList = mutableListOf<ArticleViewData>()

    @Inject
    var presenter: IArticlesPresenter? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_articles, container, false)

        listRecyclerView = view.findViewById(R.id.listRecyclerView)
        listSwipeRefreshLayout = view.findViewById(R.id.listSwipeRefreshLayout)
        emptyListText = view.findViewById(R.id.emptyListText)

        listAdapter = ArticlesAdapter(dataList)
        listRecyclerView?.setLayoutManager(LinearLayoutManager(activity, RecyclerView.VERTICAL, false))
        listRecyclerView?.setAdapter(listAdapter)

        emptyListText?.setVisibility(View.GONE)

        listSwipeRefreshLayout?.setColorSchemeResources(android.R.color.holo_orange_dark)
        listSwipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            presenter?.loadArticles(true)
        })

        return view
    }


    override fun onResume() {
        super.onResume()
        presenter?.loadArticles( false )

    }

    override fun onPause() {
        super.onPause()
        presenter?.dispose()
    }

    //region IArticlesListView implementation
    override fun loading(isLoading: Boolean) {

        listSwipeRefreshLayout?.setRefreshing(isLoading)
    }

    override fun onArticlesLoaded(articleListViewModel: ArticleListViewModel) {

        dataList.clear()

        if (articleListViewModel == null) {

            listAdapter?.notifyDataSetChanged()
            return
        }

        dataList.addAll(articleListViewModel.articles)
        listAdapter?.notifyDataSetChanged()

        val listIsEmpty = articleListViewModel.articles.size == 0
        emptyListText?.setVisibility(if (listIsEmpty) View.VISIBLE else View.GONE)
    }


    override fun onError(errorMessage: String) {

        dataList.clear()
        listAdapter?.notifyDataSetChanged()
        emptyListText?.setVisibility(View.VISIBLE)

        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
    }
    //endregion IArticlesListView implementation

}
