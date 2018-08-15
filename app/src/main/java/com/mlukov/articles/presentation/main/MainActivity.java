package com.mlukov.articles.presentation.main;

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
import com.mlukov.articles.presentation.articles.list.view.ArticlesAdapter;
import com.mlukov.articles.presentation.articles.list.view.IArticlesListView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements IArticlesListView {


    //region AppCompatActivity overrides
    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {

        AndroidInjection.inject( this );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }
    //region AppCompatActivity overrides
}
