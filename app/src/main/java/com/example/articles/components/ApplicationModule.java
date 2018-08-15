package com.example.articles.components;

import com.example.articles.ApplicationContext;
import com.example.articles.domain.repositories.IRepoFactory;
import com.example.articles.presentation.configuration.IPresentationConfigurator;
import com.example.articles.presentation.configuration.PresentationConfigurator;
import com.example.articles.repositories.RepoFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final ApplicationContext mApplication;

    public ApplicationModule(ApplicationContext application) {

        this.mApplication = application;
    }

    @Provides
    @Singleton
    IRepoFactory repoFactory(){

        return new RepoFactory( mApplication );
    }

    @Provides
    @Singleton
    IPresentationConfigurator presentationConfigurator(){

        return new PresentationConfigurator();
    }
}
