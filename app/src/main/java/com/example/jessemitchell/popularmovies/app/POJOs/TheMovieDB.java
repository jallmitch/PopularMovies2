package com.example.jessemitchell.popularmovies.app.POJOs;

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
    @GET("3/movie/{listType}")
    Call<MovieDetails> getMovies(@Path("listType") String listType, @QueryMap Map<String, String> options);
}
