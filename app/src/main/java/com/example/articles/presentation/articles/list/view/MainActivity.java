package com.example.articles.presentation.articles.list.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.articles.ApplicationContext;
import com.example.articles.R;
import com.example.articles.presentation.configuration.IPresentationConfigurator;
import com.example.articles.presentation.articles.list.presenter.IArticlesPresenter;
import com.example.articles.presentation.articles.list.model.ArticleViewData;
import com.example.articles.presentation.articles.list.model.ArticleListViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements IArticlesListView {

    // Views
    private RecyclerView mListRecyclerView = null;
    private SwipeRefreshLayout mListSwipeRefreshLayout;
    private TextView mEmptyListText;

    private ArticlesAdapter mListAdapter = null;
    private List<ArticleViewData > mDataList = new ArrayList();

    private IArticlesPresenter mPresenter;

    @Inject
    IPresentationConfigurator mPresentationConfigurator;

    //region AppCompatActivity overrides
    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        ApplicationContext.getAppContext().component().inject( this );

        mListRecyclerView = findViewById( R.id.listRecyclerView );
        mListSwipeRefreshLayout = findViewById( R.id.listSwipeRefreshLayout );
        mEmptyListText = findViewById( R.id.emptyListText );

        mListAdapter = new ArticlesAdapter( mDataList );
        initListRecyclerView();

        mEmptyListText.setVisibility( View.GONE );

        mPresentationConfigurator.configureUsersListView(this );

        mPresenter.onViewCreated();

        mListSwipeRefreshLayout.setColorSchemeResources( android.R.color.holo_orange_dark );
        mListSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if( mPresenter != null )
                    mPresenter.onRefresh();
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        mPresenter.onViewShown();
    }

    @Override
    public void onConfigurationChanged( Configuration newConfig ) {

        super.onConfigurationChanged( newConfig );

        initListRecyclerView();
    }

    @Override
    protected void onPause() {

        mPresenter.onViewHidden();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        mPresenter.onViewDestroyed();
        super.onDestroy();
    }
    //region AppCompatActivity overrides


    //region IArticlesListView implementation
    @Override
    public void setPresenter( IArticlesPresenter presenter ) {

        mPresenter = presenter;
    }

    @Override
    public void onStartLoadingUsers() {

        showProgressBar();
    }

    @Override
    public void onUsersLoaded( ArticleListViewModel listViewModel ) {

        mDataList.clear();

        if( listViewModel == null ){

            mListAdapter.notifyDataSetChanged();
            return;
        }

        mDataList.addAll( listViewModel.getUsersList() );
        mListAdapter.notifyDataSetChanged();

        boolean listIsEmpty = listViewModel.getUsersList().size() == 0;
        mEmptyListText.setVisibility( listIsEmpty ? View.VISIBLE : View.GONE );

        hideProgressBar();
    }

    @Override
    public void onUserLoadError( String errorMessage ) {

        hideProgressBar();

        mDataList.clear();
        mListAdapter.notifyDataSetChanged();
        mEmptyListText.setVisibility( View.VISIBLE );

        Toast.makeText( this, errorMessage, Toast.LENGTH_LONG ).show();
    }
    //endregion IArticlesListView implementation

    private void initListRecyclerView() {

        mListRecyclerView.setLayoutManager(  new LinearLayoutManager( this, RecyclerView.VERTICAL, false  ) );
        mListRecyclerView.setAdapter( mListAdapter );
    }

    private void showProgressBar(){

        mListSwipeRefreshLayout.setRefreshing( true );
    }

    private void hideProgressBar(){

        mListSwipeRefreshLayout.setRefreshing( false );
    }
}
