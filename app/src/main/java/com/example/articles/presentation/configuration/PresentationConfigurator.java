package com.example.articles.presentation.configuration;


import com.example.articles.ApplicationContext;
import com.example.articles.domain.repositories.IRepoFactory;
import com.example.articles.presentation.articles.list.presenter.IArticlesPresenter;
import com.example.articles.presentation.articles.list.view.IArticlesListView;
import com.example.articles.presentation.articles.list.presenter.ArticlesPresenter;

import javax.inject.Inject;

public class PresentationConfigurator implements IPresentationConfigurator {

    @Inject
    IRepoFactory repoFactory;


    public PresentationConfigurator() {

        ApplicationContext.getAppContext().component().inject( this );
    }

    @Override
    public void configureUsersListView( IArticlesListView usersView ){

        IArticlesPresenter usersPresenter = new ArticlesPresenter( usersView, repoFactory );
        usersView.setPresenter( usersPresenter );
    }
}
