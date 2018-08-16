package com.mlukov.articles.presentation.articles.list.view


import android.content.Context
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
import com.mlukov.articles.presentation.articles.list.presenter.IArticlesListPresenter
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ArticlesListFragment : Fragment(), IArticlesListView {

    companion object {

        val TAG = ArticlesListFragment::class.java.simpleName

        fun newInstance(): ArticlesListFragment{
            return ArticlesListFragment()
        }
    }

    private var listRecyclerView: RecyclerView? = null
    private var listSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var emptyListText: TextView? = null

    @Inject
    lateinit var listAdapter: ArticlesAdapter

    @Inject
    lateinit var listPresenter: IArticlesListPresenter

    override fun onAttach(context: Context) {

        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {

        val view = inflater.inflate(R.layout.fragment_articles_list, container, false)

        listRecyclerView = view.findViewById(R.id.listRecyclerView)
        listSwipeRefreshLayout = view.findViewById(R.id.listSwipeRefreshLayout)
        emptyListText = view.findViewById(R.id.emptyListText)

        listRecyclerView?.setLayoutManager(LinearLayoutManager(activity, RecyclerView.VERTICAL, false))
        listRecyclerView?.setAdapter(listAdapter)

        emptyListText?.setVisibility(View.GONE)

        listSwipeRefreshLayout?.setColorSchemeResources(android.R.color.holo_orange_dark)
        listSwipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            listPresenter.loadArticles(true)
        })

        return view
    }

    override fun onResume() {

        super.onResume()
        listPresenter.loadArticles( false )

    }

    override fun onPause() {

        super.onPause()
        listPresenter.dispose()
    }

    //region IArticlesListView implementation
    override fun onLoadingStateChange(isLoading: Boolean) {

        listSwipeRefreshLayout?.setRefreshing(isLoading)
    }

    override fun onArticlesLoaded(articleListViewModel: ArticleListViewModel) {

        listAdapter?.updateList( articleListViewModel.articles )

        val listIsEmpty = articleListViewModel.articles.size == 0
        emptyListText?.setVisibility(if (listIsEmpty) View.VISIBLE else View.GONE)
    }


    override fun onError(errorMessage: String) {

        listAdapter?.notifyDataSetChanged()

        emptyListText?.setVisibility( if( listAdapter.itemCount == 0 ) View.VISIBLE else View.INVISIBLE )

        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
    }
    //endregion IArticlesListView implementation

}
