package com.mlukov.articles.presentation.main

import android.os.Bundle
import android.support.v4.app.Fragment

import com.mlukov.articles.R
import com.mlukov.articles.presentation.articles.details.model.ArticleDetailsViewData
import com.mlukov.articles.presentation.articles.details.view.ArticleDetailsFragment
import com.mlukov.articles.presentation.articles.list.view.ArticlesListFragment
import com.mlukov.articles.presentation.base.BaseActivity

import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class MainActivity : BaseActivity(), HasSupportFragmentInjector, IMainNavigator {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    //region AppCompatActivity overrides
    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {

        super.onResume()
        showArticleList()
    }

    override fun onPause() {

        super.onPause()
    }
    //region AppCompatActivity overrides


    //region HasSupportFragmentInjector implementation

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {

        return dispatchingAndroidInjector
    }
    //endregion HasSupportFragmentInjector implementation


    //region IMainNavigator implementation
    override fun showArticleList() {

        var articlesFragment = supportFragmentManager.findFragmentByTag( ArticlesListFragment.TAG )
        if (articlesFragment == null) {

            articlesFragment = ArticlesListFragment.newInstance()
        }

        showScreen(articlesFragment, ArticlesListFragment.TAG, false, false)
    }

    override fun showArticleDetails( articleDetailsViewData: ArticleDetailsViewData) {

        var articleDetailsFragment = supportFragmentManager.findFragmentByTag( ArticleDetailsFragment.TAG )
        if (articleDetailsFragment == null) {

            articleDetailsFragment = ArticleDetailsFragment.newInstance( articleDetailsViewData )
        }

        showScreen(articleDetailsFragment, ArticleDetailsFragment.TAG, true, true )
    }
    //endregion IMainNavigator implementation

}
