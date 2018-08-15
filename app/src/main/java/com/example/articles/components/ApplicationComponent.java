package com.example.articles.components;


import com.example.articles.ApplicationContext;
import com.example.articles.domain.repositories.IRepoFactory;
import com.example.articles.presentation.configuration.IPresentationConfigurator;
import com.example.articles.presentation.configuration.PresentationConfigurator;
import com.example.articles.presentation.articles.list.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(
        modules = { ApplicationModule.class }
)

public interface ApplicationComponent{

    void inject( ApplicationContext application );
    void inject( MainActivity mainActivity );
    void inject( PresentationConfigurator presentationConfigurator );

    IRepoFactory repoFactory();
    IPresentationConfigurator presentationConfigurator();
}
