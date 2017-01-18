package com.example.jessemitchell.popularmovies.app.interfaces;

import com.example.jessemitchell.popularmovies.app.POJOs.MovieDetailResults;
import com.example.jessemitchell.popularmovies.app.POJOs.VideoDetailResults;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by jesse.mitchell on 1/16/2017.
 */

public interface TheMovieDB
{
    //https://api.themoviedb.org/3/movie/popular?api_key=API_KEY&language=en-US
    @GET("3/movie/{contentType}")
    Call<MovieDetailResults> getMovies(@Path("contentType") String listType,
                                       @QueryMap Map<String, String> parameters);

    //https://api.themoviedb.org/3/movie/297761/videos?api_key=API_KEY&language=en-US
    //https://api.themoviedb.org/3/movie/297761/reviews?api_key=API_KEY&language=en-US
    @GET("3/movie/{id}/{contentType}")
    Call<VideoDetailResults> getContent(@Path("id") String movidId,
                                        @Path("contentType") String contentType,
                                        @QueryMap Map<String, String> parameters);
}
