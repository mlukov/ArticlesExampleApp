package com.mlukov.articles.presentation.articles.list.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mlukov.articles.R;
import com.mlukov.articles.presentation.articles.list.presenter.IArticlesPresenter;
import com.mlukov.articles.presentation.articles.list.model.ArticleViewData;
import com.mlukov.articles.presentation.articles.list.model.ArticleListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements IArticlesListView {

    // Views
    private RecyclerView mListRecyclerView = null;
    private SwipeRefreshLayout mListSwipeRefreshLayout;
    private TextView mEmptyListText;

    private ArticlesAdapter mListAdapter = null;
    private List<ArticleViewData > mDataList = new ArrayList();

    @Inject
    public IArticlesPresenter mPresenter;


    //region AppCompatActivity overrides
    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {

        AndroidInjection.inject( this );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mListRecyclerView = findViewById( R.id.listRecyclerView );
        mListSwipeRefreshLayout = findViewById( R.id.listSwipeRefreshLayout );
        mEmptyListText = findViewById( R.id.emptyListText );

        mListAdapter = new ArticlesAdapter( mDataList );
        mListRecyclerView.setLayoutManager(  new LinearLayoutManager( this, RecyclerView.VERTICAL, false  ) );
        mListRecyclerView.setAdapter( mListAdapter );

        mEmptyListText.setVisibility( View.GONE );

        mListSwipeRefreshLayout.setColorSchemeResources( android.R.color.holo_orange_dark );
        mListSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if( mPresenter != null )
                    mPresenter.loadArticles( true );
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        mPresenter.loadArticles( false );
    }

    @Override
    protected void onPause() {

        mPresenter.dispose();
        super.onPause();
    }

    //region AppCompatActivity overrides


    //region IArticlesListView implementation
    @Override
    public void loading( boolean isLoading ) {

        mListSwipeRefreshLayout.setRefreshing( isLoading );
    }

    @Override
    public void onArticlesLoaded( @NotNull ArticleListViewModel articleListViewModel ) {

        mDataList.clear();

        if( articleListViewModel == null ){

            mListAdapter.notifyDataSetChanged();
            return;
        }

        mDataList.addAll( articleListViewModel.getArticles() );
        mListAdapter.notifyDataSetChanged();

        boolean listIsEmpty = articleListViewModel.getArticles().size() == 0;
        mEmptyListText.setVisibility( listIsEmpty ? View.VISIBLE : View.GONE );
    }



    @Override
    public void onError( String errorMessage ) {

        mDataList.clear();
        mListAdapter.notifyDataSetChanged();
        mEmptyListText.setVisibility( View.VISIBLE );

        Toast.makeText( this, errorMessage, Toast.LENGTH_LONG ).show();
    }
    //endregion IArticlesListView implementation
}
