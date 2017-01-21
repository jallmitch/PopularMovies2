package com.example.jessemitchell.popularmovies.app.presentors;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jessemitchell.popularmovies.app.R;
import com.example.jessemitchell.popularmovies.app.data.MovieDetailResults;

import java.util.ArrayList;

/**
 * Created by jesse.mitchell on 1/21/2017.
 */

public class MovieListAdapter extends CursorAdapter
{

    private static final int MOVIE_ID = 0;
    private static final int MOVIE_TITLE = 1;
    private static final int MOIVE_OVERVIEW = 2;
    private static final int MOVIE_RELEASE_DATE = 3;
    private static final int MOVIE_POSTER_PATH = 4;
    private static final int MOVIE_VOTE_AVERAGE = 5;

    private ImageAdapter movieDetailsAdapter;

    public MovieListAdapter(Context context, Cursor cursor, int flags)
    {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View rootView = LayoutInflater.from(context).inflate(R.layout.movies_main, parent, false);
        movieDetailsAdapter = new  ImageAdapter(context, new ArrayList<MovieDetailResults.MovieDetail>());

        int count = cursor.getCount();
        MovieDetailsViewHolder viewHolder = new MovieDetailsViewHolder(rootView);
        rootView.setTag(viewHolder);
        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        MovieDetailsViewHolder viewHolder = (MovieDetailsViewHolder) view.getTag();

        int count = cursor.getCount();
        if (cursor != null && cursor.moveToFirst()) {
            MovieDetailResults.MovieDetail movieDetail = new MovieDetailResults.MovieDetail();

            movieDetail.setId(cursor.getInt(MOVIE_ID));
            movieDetail.setTitle(cursor.getString(MOVIE_TITLE));
            movieDetail.setOverview(cursor.getString(MOIVE_OVERVIEW));
            movieDetail.setReleaseDate(cursor.getString(MOVIE_RELEASE_DATE));
            movieDetail.setPosterPath(cursor.getString(MOVIE_POSTER_PATH));
            movieDetail.setVoteAverage(cursor.getDouble(MOVIE_VOTE_AVERAGE));

            movieDetailsAdapter.add(movieDetail);
        }
    }


}
