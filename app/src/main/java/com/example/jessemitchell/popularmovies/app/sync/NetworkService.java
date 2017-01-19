package com.example.jessemitchell.popularmovies.app.sync;

import android.util.LruCache;

import com.example.jessemitchell.popularmovies.app.POJOs.MovieDetailResults;
import com.example.jessemitchell.popularmovies.app.POJOs.ReviewDetailResults;
import com.example.jessemitchell.popularmovies.app.POJOs.VideoDetailResults;

import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jesse.mitchell on 1/18/2017.
 */

public class NetworkService
{
    private static String baseUrl = "https://api.themoviedb.org/";
    private TheMovieDB theMovieDB;
    private LruCache<Class<?>, Observable<?>> apiObserables = new LruCache<>(10);

    public NetworkService()
    {
        this(baseUrl);
    }

    public NetworkService(String baseUrl)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        theMovieDB = retrofit.create(TheMovieDB.class);
    }

    public TheMovieDB getAPI()
    {
        return theMovieDB;
    }

    public interface TheMovieDB
    {
        //https://api.themoviedb.org/3/movie/popular?api_key=API_KEY&language=en-US
        @GET("3/movie/{contentType}")
        Observable<MovieDetailResults> getMovies(@Path("contentType") String listType,
                                                 @QueryMap Map<String, String> parameters);

        //https://api.themoviedb.org/3/movie/297761/videos?api_key=API_KEY&language=en-US
        @GET("3/movie/{id}/{contentType}")
        Observable<VideoDetailResults> getVideos(@Path("id") int movidId,
                                                 @Path("contentType") String contentType,
                                                 @QueryMap Map<String, String> parameters);

        //https://api.themoviedb.org/3/movie/297761/reviews?api_key=API_KEY&language=en-US
        @GET("3/movie/{id}/{contentType}")
        Observable<ReviewDetailResults> getReviews(@Path("id") int movidId,
                                                   @Path("contentType") String contentType,
                                                   @QueryMap Map<String, String> parameters);
    }

    public Observable<?> getPreparedObservable(Observable<?> unPreparedObservable,
                                               Class<?> clazz,
                                               boolean cacheObservable,
                                               boolean useCache)
    {
        Observable<?> preparedObservable = null;

        if(useCache)
            preparedObservable = apiObserables.get(clazz);

        if(preparedObservable!=null)
            return preparedObservable;

        preparedObservable = unPreparedObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        return  preparedObservable;
    }
}
