package com.example.articles.repositories;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.articles.BuildConfig;
import com.example.articles.domain.models.ArticleData;
import com.example.articles.domain.repositories.IArticleDetailsResponseHandler;
import com.example.articles.domain.repositories.IArticleListResponseHandler;
import com.example.articles.domain.repositories.IArticlesApiRepo;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ArticlesApiRepo implements IArticlesApiRepo {

    private final static String TAG = ArticlesApiRepo.class.getSimpleName();

    private Retrofit mRetrofit;
    private String mApiEndpoint;

    public ArticlesApiRepo(){

        mApiEndpoint = BuildConfig.API_ENDPOINT;
        mRetrofit = buildRetrofit();
    }

    //region IArticlesApiRepo implementation
    // callbacks executed on main thread
    @Override
    public Disposable getArticleList( @NonNull final IArticleListResponseHandler responseHandler ) {

        assert responseHandler != null;

        if( mRetrofit == null ){

            String message = " listUsers() mRetrofit is null";
            Log.e( TAG, message );

            if( responseHandler != null )
                responseHandler.onError();

            return null;
        }

        ArticlesApiController apiController = this.mRetrofit.create( ArticlesApiController.class );

        if( apiController == null ){

            Log.e( TAG," getArticleList() apiController is null" );

            if( responseHandler != null )
                responseHandler.onError();
        }

        Call call = null;
        try{
            call = apiController.getArticleList();
        }
        catch ( Exception ex ){

            Log.e( TAG, ex.getMessage(), ex );
        }

        if( call == null ){

            Log.e( TAG," getArticleList() call is null" );

            if( responseHandler != null )
                responseHandler.onError();
        }

        final Call callFinal = call;


        Observable observable = Observable.create( new ObservableOnSubscribe<List<ArticleData >>() {

            @Override
            public void subscribe( final ObservableEmitter< List< ArticleData > > e ) throws Exception {

                callFinal.enqueue( new Callback<List<ArticleData >>() {

                    public void onFailure( @Nullable Call call, @Nullable Throwable throwable) {

                        e.onError( throwable );
                    }

                    public void onResponse( @Nullable Call<List<ArticleData >> call, @Nullable Response<List<ArticleData >> response) {

                        e.onNext( response.body() );
                        e.onComplete();
                    }
                });
            }
        } ) ;

        return observable.subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Consumer< List< ArticleData > >() {
                                @Override
                                public void accept( List< ArticleData > data ) throws Exception {

                                    responseHandler.onListLoaded( data );
                                }
                            },
                        new Consumer< Throwable >() {
                            @Override
                            public void accept( Throwable throwable ) throws Exception {


                                Log.e( TAG, throwable.getMessage(), throwable );
                                responseHandler.onError();
                            }
                        });
    }

    @Override
    public Disposable getArticleDetails( int articleId, IArticleDetailsResponseHandler resultHandler ) {

        return null;
    }

    //endregion IArticlesApiRepo implementation


    private final Retrofit buildRetrofit() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy( FieldNamingPolicy.IDENTITY);
        Gson gson = gsonBuilder.create();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2L, TimeUnit.MINUTES)
                .readTimeout(2L, TimeUnit.MINUTES)
                .writeTimeout(2L, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder().baseUrl(mApiEndpoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client).build();

    }
}
