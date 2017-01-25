package com.example.jessemitchell.popularmovies.app.presentors;

/**
 * Created by jesse.mitchell on 1/19/2017.
 */

public interface MovieListInteractor
{
    void loadMovieList();
    void setListType(String listType);
    void unSubscribeMovieList();
}
