package com.example.jessemitchell.popularmovies.app.presentors;

import com.example.jessemitchell.popularmovies.app.PopularMoviesFragment;
import com.example.jessemitchell.popularmovies.app.data.MovieDetailResults;
import com.example.jessemitchell.popularmovies.app.data.NetworkService;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static com.example.jessemitchell.popularmovies.app.BuildConfig.MOVIE_DB_API_KEY;

/**
 * Created by jesse.mitchell on 1/19/2017.
 */

public class MovieListPresenter implements MovieListInteractor
{
    private PopularMoviesFragment fragment;
    private String mListType;
    private NetworkService service;
    private Subscription subscription;

    public MovieListPresenter(PopularMoviesFragment fragment, String listType)
    {
        this.fragment = fragment;
        this.service = new NetworkService();
        this.mListType = listType;
    }

    @Override
    public void loadMovieList()
    {
        final String API_KEY_PARM = "api_key";
        final String LANG_PARAM = "language";
        final String LANG = "en-US";
        Map<String, String> params = new HashMap<>();
        params.put(API_KEY_PARM, MOVIE_DB_API_KEY);
        params.put(LANG_PARAM, LANG);

        Observable<MovieDetailResults> moviesObservable =
                (Observable<MovieDetailResults>)service
                        .getPreparedObservable(service.getAPI()
                                        .getMovies(mListType, params)
                                ,MovieDetailResults.class, true, true );
        subscription = moviesObservable.subscribe(new Observer<MovieDetailResults>() {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(MovieDetailResults movieDetailResults)
            {
                fragment.addMovies(movieDetailResults);
            }
        });

    }

    @Override
    public void unSubscribeMovieList()
    {
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
