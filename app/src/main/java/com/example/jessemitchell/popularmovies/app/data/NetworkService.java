package com.example.jessemitchell.popularmovies.app.data;

import android.util.LruCache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.jessemitchell.popularmovies.app.BuildConfig.MOVIE_DB_API_KEY;

/**
 * Created by jesse.mitchell on 1/18/2017.
 *
 * Retrofit2 with rxjava resource
 * http://www.captechconsulting.com/blogs/a-mvp-approach-to-lifecycle-safe-requests-with-retrofit-20-and-rxjava
 */

public class NetworkService
{
    private static String baseUrl = "https://api.themoviedb.org/";
    private TheMovieDB theMovieDB;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private LruCache<Class<?>, Observable<?>> apiObservables = new LruCache<>(10);

    public NetworkService()
    {
        this(baseUrl);
    }

    public NetworkService(String baseUrl)
    {
        final String API_KEY_PARM = "api_key";
        final String LANG_PARAM = "language";
        final String LANG = "en-US";
        Map<String, String> params = new HashMap<>();
        params.put(API_KEY_PARM, MOVIE_DB_API_KEY);
        params.put(LANG_PARAM, LANG);

        okHttpClient = buildClient();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        theMovieDB = retrofit.create(TheMovieDB.class);

    }

    public Retrofit getRetrofit()
    {
        return retrofit;
    }

    public TheMovieDB getAPI()
    {
        return theMovieDB;
    }

    public OkHttpClient buildClient(){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                // Do anything with response here
                //if we ant to grab a specific cookie or something..
                return response;
            }
        });

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //this is where we will add whatever we want to our request headers.
                Request request = chain.request().newBuilder().addHeader("Accept", "application/json").build();
                return chain.proceed(request);
            }
        });

        return  builder.build();
    }


    public void clearCache(){
        apiObservables.evictAll();
    }

    public Observable<?> getPreparedObservable(Observable<?> unPreparedObservable,
                                               Class<?> clazz,
                                               boolean cacheObservable,
                                               boolean useCache)
    {
        Observable<?> preparedObservable = null;

        if(useCache)
            preparedObservable = apiObservables.get(clazz);

        if(preparedObservable!=null)
            return preparedObservable;

        preparedObservable = unPreparedObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        return  preparedObservable;
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
}
