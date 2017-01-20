package com.example.jessemitchell.popularmovies.app.presentors;

import com.example.jessemitchell.popularmovies.app.MovieDetailFragment;
import com.example.jessemitchell.popularmovies.app.data.NetworkService;
import com.example.jessemitchell.popularmovies.app.data.ReviewDetailResults;
import com.example.jessemitchell.popularmovies.app.data.VideoDetailResults;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.example.jessemitchell.popularmovies.app.BuildConfig.MOVIE_DB_API_KEY;

/**
 * Created by jesse.mitchell on 1/19/2017.
 *
 * Resource for using Observables
 * https://newfivefour.com/android-rxjava-wait-for-network-calls-finish.html
 */

public class MovieDetailsPresenter implements MovieDetailsInteractor
{
    private MovieDetailFragment fragment;
    private NetworkService service;
    private Retrofit retrofit;
    private Subscription reviewSubscrip;
    private Subscription trailerSubscrip;
    private int movieId;

    public MovieDetailsPresenter(MovieDetailFragment fragment, NetworkService service, int movieId)
    {
        this.fragment = fragment;
        this.service = new NetworkService();
        this.movieId = movieId;
        this.retrofit = this.service.getRetrofit();
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


        Observable<VideoDetailResults> trailerResults = retrofit
                .create(NetworkService.TheMovieDB.class)
                .getVideos(movieId, "videos", params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());


        Observable<ReviewDetailResults> reviewResults = retrofit
                .create(NetworkService.TheMovieDB.class)
                .getReviews(movieId, "reviews", params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<TrailersAndReviews> combined  = Observable.zip(reviewResults, trailerResults, new Func2<ReviewDetailResults,VideoDetailResults, TrailersAndReviews>(){
            @Override
            public TrailersAndReviews call(ReviewDetailResults reviewDetailResults, VideoDetailResults videoDetailResults) {
                return new TrailersAndReviews(reviewDetailResults,videoDetailResults);
            }
        });

        reviewSubscrip = combined.subscribe(new Subscriber<TrailersAndReviews>() {
            @Override
            public void onNext(TrailersAndReviews trailersAndReviews) {

                fragment.loadRetroData(trailersAndReviews);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }
        });

    }

    public class TrailersAndReviews
    {
        public ReviewDetailResults reviews;
        public VideoDetailResults trailers;

        public TrailersAndReviews(ReviewDetailResults reviews, VideoDetailResults trailers)
        {
            this.reviews = reviews;
            this.trailers = trailers;
        }
    }

    @Override
    public void unSubscribeMovieReviews()
    {

        if(reviewSubscrip!=null && !reviewSubscrip.isUnsubscribed())
            reviewSubscrip.unsubscribe();
    }

}
