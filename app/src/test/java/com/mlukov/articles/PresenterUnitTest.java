package com.mlukov.articles;

import com.mlukov.articles.domain.models.ArticleData;
import com.mlukov.articles.domain.repositories.articles.IArticlesApiRepository;
import com.mlukov.articles.presentation.providers.INetworkInfoProvider;
import com.mlukov.articles.domain.repositories.IRepoFactory;
import com.mlukov.articles.presentation.providers.IResourceProvider;
import com.mlukov.articles.domain.repositories.IArticleListResponseHandler;
import com.mlukov.articles.presentation.articles.list.presenter.ArticlesPresenter;
import com.mlukov.articles.presentation.articles.list.view.IArticlesListView;
import com.mlukov.articles.presentation.articles.list.model.ArticleListViewModel;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PresenterUnitTest {

    private IRepoFactory    repoFactory;
    private IArticlesListView usersView;
    private IArticlesApiRepository userApiRepo;
    private INetworkInfoProvider deviceRepo;
    private IResourceProvider resourceRepo;

    private String          errorMessage = "Error";


    @Test
    public void test_onViewDestroyed_onViewCreated(){

        initInterfaces();

        ArticlesPresenter presenter = new ArticlesPresenter( usersView, repoFactory );

        presenter.onViewDestroyed();
        presenter.onViewCreated();
    }

    @Test
    public void test_onViewDestroyed_onViewHidden(){

        initInterfaces();

        ArticlesPresenter presenter = new ArticlesPresenter( usersView, repoFactory );

        presenter.onViewDestroyed();
        presenter.onViewHidden();
    }

    @Test
    public void test_onViewDestroyed_onViewShown(){

        initInterfaces();

        ArticlesPresenter presenter = new ArticlesPresenter( usersView, repoFactory );

        presenter.onViewDestroyed();
        presenter.onViewShown();
    }

    @Test
    public void test_OnViewCreated__Api_OnNext_One_onCompleteLoading(){

        initInterfaces();

        final String userNames = "John Smith";

        doAnswer( new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {

                // After finish loading users, error should not be displayed
                IArticleListResponseHandler callback = (IArticleListResponseHandler ) invocation.getArguments()[0];

                List<ArticleData > usersList = new ArrayList<>(  );
                ArticleData user = new ArticleData(userNames );
                usersList.add( user );

                callback.onListLoaded( usersList );
                callback.onCompleteLoading();
                return null;
            }
        } ).when( userApiRepo ).getArticleList( any( IArticleListResponseHandler.class ) );

        ArticlesPresenter presenter = new ArticlesPresenter( usersView, repoFactory );

        ArgumentCaptor<ArticleListViewModel > viewModelCaptor = ArgumentCaptor.forClass( ArticleListViewModel.class );

        presenter.onViewCreated();
        presenter.onViewShown();

        Mockito.verify( usersView ).onUsersLoaded( viewModelCaptor.capture() );

        // List should with one user
        assertEquals( 1, viewModelCaptor.getValue().getUsersList().size() );
        // Names should be as
        assertEquals( userNames, viewModelCaptor.getValue().getUsersList().get( 0 ).getTitle() );

        // After finish loading, errors should not be displayed
        verify( usersView, times( 0 ) ).onUserLoadError( anyString() );
    }

    @Test
    public void test_OnViewCreated__Api_OnError_OnNext_One_onCompleteLoading(){

        initInterfaces();

        doAnswer( new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {

                // After finish loading users, error should not be displayed
                IArticleListResponseHandler callback = (IArticleListResponseHandler ) invocation.getArguments()[0];

                List<ArticleData > usersList = new ArrayList<>(  );
                ArticleData user = new ArticleData( "John Smith" );
                usersList.add( user );

                callback.onError();
                callback.onListLoaded( usersList );
                callback.onCompleteLoading();

                return null;
            }
        } ).when( userApiRepo ).getArticleList( any( IArticleListResponseHandler.class ) );

        ArticlesPresenter presenter = new ArticlesPresenter( usersView, repoFactory );

        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass( String.class );

        presenter.onViewCreated();
        presenter.onViewShown();

        verify( usersView ).onUserLoadError( errorMessageCaptor.capture() );

        // One error message should be send to interface
        assertEquals( 1, errorMessageCaptor.getAllValues().size() );
        assertEquals( errorMessage, errorMessageCaptor.getValue() );

        // After error, results from api repo should not be send to view until next load
        verify( usersView, times( 0 ) ).onStartLoadingUsers();
        verify( usersView, times( 0 ) ).onUsersLoaded( any( ArticleListViewModel.class ));
    }

    @Test
    public void test_OnViewCreated__Api_OnNext_Null_onCompleteLoading(){

        initInterfaces();

        doAnswer( new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {

                // After finish loading users, error should not be displayed
                IArticleListResponseHandler callback = (IArticleListResponseHandler ) invocation.getArguments()[0];
                callback.onListLoaded( null );
                callback.onCompleteLoading();
                callback.onError();
                return null;
            }
        } ).when( userApiRepo ).getArticleList( any( IArticleListResponseHandler.class ) );

        ArticlesPresenter presenter = new ArticlesPresenter( usersView, repoFactory );

        ArgumentCaptor<ArticleListViewModel > viewModelCaptor = ArgumentCaptor.forClass( ArticleListViewModel.class );

        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass( String.class );

        presenter.onViewCreated();
        presenter.onViewShown();

        Mockito.verify( usersView ).onUsersLoaded( viewModelCaptor.capture() );

        // List should be empty, since null result is passed from repo
        assertEquals( 0, viewModelCaptor.getValue().getUsersList().size() );

        // After finish loading, errors should not be displayed
        verify( usersView, times( 0 ) ).onUserLoadError( anyString() );
    }

    @Test
    public void test_OnViewCreated__Api_OnComplete_OnNext_OnError(){

        initInterfaces();

        doAnswer( new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {

                IArticleListResponseHandler callback = (IArticleListResponseHandler ) invocation.getArguments()[0];
                callback.onCompleteLoading();

                List<ArticleData > usersList = new ArrayList<>(  );
                ArticleData user = new ArticleData("John Smith");
                usersList.add( user );

                callback.onListLoaded( usersList );
                callback.onError();
                return null;
            }
        } ).when( userApiRepo ).getArticleList( any( IArticleListResponseHandler.class ) );

        ArticlesPresenter presenter = new ArticlesPresenter( usersView, repoFactory );

        presenter.onViewCreated();

        // View is not shown, all of the functions below should not be called
        verify( usersView, times( 0 ) ).onUserLoadError( anyString() );
        verify( usersView, times( 0 ) ).onUsersLoaded( any( ArticleListViewModel.class ) );
        verify( usersView, times( 0 ) ).onStartLoadingUsers();
    }

    private void initInterfaces(){

        repoFactory     = mock( IRepoFactory.class );
        usersView       = mock( IArticlesListView.class);
        userApiRepo     = mock( IArticlesApiRepository.class );
        deviceRepo      = mock( INetworkInfoProvider.class );
        resourceRepo    = mock( IResourceProvider.class );

        when(repoFactory.getRepository( IArticlesApiRepository.class )).thenReturn( userApiRepo );
        when(repoFactory.getRepository( INetworkInfoProvider.class )).thenReturn( deviceRepo );
        when(repoFactory.getRepository( IResourceProvider.class )).thenReturn( resourceRepo );

        when( deviceRepo.isNetworkConnected()).thenReturn( false );
        when( resourceRepo.getString( any( int.class ) ) ).thenReturn( errorMessage );
    }

}
