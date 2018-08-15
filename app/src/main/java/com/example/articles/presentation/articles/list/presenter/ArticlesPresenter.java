package com.example.articles.presentation.articles.list.presenter;

import android.support.annotation.Nullable;

import com.example.articles.R;
import com.example.articles.domain.repositories.IArticlesApiRepo;
import com.example.articles.domain.repositories.IRepoFactory;
import com.example.articles.domain.models.ArticleData;
import com.example.articles.domain.repositories.IArticleListResponseHandler;
import com.example.articles.domain.repositories.IDeviceRepo;
import com.example.articles.domain.repositories.IResourceRepo;
import com.example.articles.presentation.articles.list.view.IArticlesListView;
import com.example.articles.presentation.articles.list.model.ArticleViewData;
import com.example.articles.presentation.articles.list.model.ArticleListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ArticlesPresenter implements IArticlesPresenter {

    enum LoadState{
        Idle,
        Loading,
        Error,
        Loaded
    }

    private final static String TAG = ArticlesPresenter.class.getSimpleName();

    private ArticleListViewModel mUsersViewModel = new ArticleListViewModel(  new ArrayList() );

    private IArticlesListView mUsersView;
    private IRepoFactory mRepoFactory;

    private boolean mViewShown = false;
    private LoadState mLoadState;
    private String mErrorMessage;

    @Nullable
    private Disposable mLoadUsersDisposable = null;

    public ArticlesPresenter( IArticlesListView usersView, IRepoFactory repoFactory ){

        assert usersView != null && repoFactory != null;

        mUsersView = usersView;
        mRepoFactory = repoFactory;

        mLoadState = LoadState.Idle;
    }

    @Override
    public void onViewCreated() {

        startLoadList();
    }

    @Override
    public void onViewShown() {

        mViewShown = true;
        switch ( mLoadState ){

            case Loading:
                mUsersView.onStartLoadingUsers();
                break;

            case Loaded:
                mUsersView.onUsersLoaded( mUsersViewModel );
                break;

            case Error:
                mUsersView.onUserLoadError( mErrorMessage );
                break;
        }
    }

    @Override
    public void onRefresh() {

        startLoadList();
    }

    @Override
    public void onViewHidden() {

        mViewShown = false;
    }

    @Override
    public void onViewDestroyed() {

        if( mLoadUsersDisposable != null )
            mLoadUsersDisposable.dispose();

        if( mUsersViewModel != null )
            mUsersViewModel.getUsersList().clear();

        mLoadUsersDisposable = null;
        mUsersViewModel = null;
    }

    private void startLoadList(){

        mLoadState = LoadState.Loading;

        if( mUsersViewModel == null )
            mUsersViewModel = new ArticleListViewModel( new ArrayList() );

        mUsersViewModel.getUsersList().clear();

        IArticlesApiRepo userApiRepo = mRepoFactory.getRepository( IArticlesApiRepo.class );

        mLoadUsersDisposable = userApiRepo.getArticleList( new IArticleListResponseHandler() {
            @Override
            public void onListLoaded( @Nullable List< ArticleData > userDataList ) {

                if( userDataList == null || mLoadState != LoadState.Loading )
                    return;

                for( ArticleData userData: userDataList )
                    mUsersViewModel.getUsersList().add(
                            new ArticleViewData( userData.getId(),
                                    userData.getTitle(),
                                    userData.getSubtitle(),
                                    userData.getDate() ));

                mLoadState = LoadState.Loaded;
                if( mViewShown ){
                    mUsersView.onUsersLoaded( mUsersViewModel );
                }
            }

            @Override
            public void onError() {

                if( mLoadState != LoadState.Loading )
                    return;

                mLoadState = LoadState.Error;

                IDeviceRepo deviceRepo = mRepoFactory.getRepository( IDeviceRepo.class );
                IResourceRepo resourceRepo = mRepoFactory.getRepository( IResourceRepo.class );

                if( deviceRepo.isNetworkConnected() == false )
                    mErrorMessage = resourceRepo.getString( R.string.internet_connection_offline );
                else
                    mErrorMessage = resourceRepo.getString( R.string.oops_went_wrong_try );

                if( mViewShown ){

                    mUsersView.onUserLoadError( mErrorMessage );
                }
            }
        } );
    }
}
