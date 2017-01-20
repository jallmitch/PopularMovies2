package com.example.jessemitchell.popularmovies.app.presentors;

import com.example.jessemitchell.popularmovies.app.MovieDetailFragment;
import com.example.jessemitchell.popularmovies.app.data.NetworkService;
import com.example.jessemitchell.popularmovies.app.data.ReviewDetailResults;
import com.example.jessemitchell.popularmovies.app.data.VideoDetailResults;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static com.example.jessemitchell.popularmovies.app.BuildConfig.MOVIE_DB_API_KEY;

/**
 * Created by jesse.mitchell on 1/19/2017.
 */

public class MovieDetailsPresenter implements MovieDetailsInteractor
{
    private MovieDetailFragment fragment;
    private NetworkService service;
    private Subscription reviewSubscrip;
    private Subscription trailerSubscrip;
    private int movieId;

    public MovieDetailsPresenter(MovieDetailFragment fragment, NetworkService service, int movieId)
    {
        this.fragment = fragment;
        this.service = new NetworkService();
        this.movieId = movieId;
    }

    @Override
    public void loadMovieReviews()
    {
        final String API_KEY_PARM = "api_key";
        final String LANG_PARAM = "language";
        final String LANG = "en-US";
        Map<String, String> params = new HashMap<>();
        params.put(API_KEY_PARM, MOVIE_DB_API_KEY);
        params.put(LANG_PARAM, LANG);

        Observable<ReviewDetailResults> results =
                (Observable<ReviewDetailResults>)service
                        .getPreparedObservable(service.getAPI()
                                        .getReviews(movieId, "reviews", params)
                                ,ReviewDetailResults.class, true, true );
        reviewSubscrip = results.subscribe(new Observer<ReviewDetailResults>() {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(ReviewDetailResults reviewDetailResults)
            {
                fragment.loadReviewData(reviewDetailResults);
            }
        });
    }

    @Override
    public void loadMovieTrailers()
    {
        final String API_KEY_PARM = "api_key";
        final String LANG_PARAM = "language";
        final String LANG = "en-US";
        Map<String, String> params = new HashMap<>();
        params.put(API_KEY_PARM, MOVIE_DB_API_KEY);
        params.put(LANG_PARAM, LANG);

        Observable<VideoDetailResults> results =
                (Observable<VideoDetailResults>)service
                        .getPreparedObservable(service.getAPI()
                                        .getReviews(movieId, "videos", params)
                                ,VideoDetailResults.class, true, true );
        trailerSubscrip = results.subscribe(new Observer<VideoDetailResults>() {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(VideoDetailResults reviewDetailResults)
            {
                fragment.loadTrailerData(reviewDetailResults);
            }
        });
    }

    @Override
    public void unSubscribeMovieReviews()
    {

        if(reviewSubscrip!=null && !reviewSubscrip.isUnsubscribed())
            reviewSubscrip.unsubscribe();
    }

    @Override
    public void unSubscribeMovieTrailers()
    {

        if(trailerSubscrip!=null && !trailerSubscrip.isUnsubscribed())
            trailerSubscrip.unsubscribe();
    }
}
