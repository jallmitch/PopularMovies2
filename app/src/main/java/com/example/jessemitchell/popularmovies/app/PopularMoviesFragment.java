package com.example.jessemitchell.popularmovies.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.jessemitchell.popularmovies.app.data.MovieContract;
import com.example.jessemitchell.popularmovies.app.data.MovieDetailResults;
import com.example.jessemitchell.popularmovies.app.presentors.ImageAdapter;
import com.example.jessemitchell.popularmovies.app.presentors.MovieListAdapter;
import com.example.jessemitchell.popularmovies.app.presentors.MovieListInteractor;
import com.example.jessemitchell.popularmovies.app.presentors.MovieListPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesse.mitchell on 12/28/2016.
 *
 * Retrofit resources
 * https://square.github.io/retrofit/
 * https://www.androidtutorialpoint.com/networking/retrofit-android-tutorial/
 */
public class PopularMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{

    private final String LOG_TAG = PopularMoviesFragment.class.getSimpleName();

    private final int LIST_LOADER = 0;
    private String mlistType;
    private MovieListInteractor moviList;
    private ImageAdapter movieDetailsAdapter;

    private GridView gView;


    private static final String[] LIST_COLUMNS =
            {
                    MovieContract.MovieEntry._ID,
                    MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                    MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                    MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE
            };

    @Override
    public void onStart()
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mlistType = sharedPrefs.getString(getString(R.string.pref_list_key),getString(R.string.pref_list_default));
        super.onStart();
        if (mlistType.equals("favorites"))
        {
            MovieListAdapter mListAdapter = new MovieListAdapter(getContext(),null, 0);
           
            gView.setAdapter(mListAdapter);
        }
        else
        {
            moviList = new MovieListPresenter(this, mlistType);
            moviList.loadMovieList();
        }

    }

    public void addMovies(MovieDetailResults movies)
    {
        List<MovieDetailResults.MovieDetail> mdResults = movies.getResults();

        movieDetailsAdapter.clear();

        for (MovieDetailResults.MovieDetail movie : mdResults)
        {
            movieDetailsAdapter.add(movie);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mlistType.equals("favorites"))
            moviList.unSubscribeMovieList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(LIST_LOADER, null, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movies_main, container, false);
        movieDetailsAdapter = new  ImageAdapter(this.getContext(), new ArrayList<MovieDetailResults.MovieDetail>());

        gView = (GridView)rootView.findViewById(R.id.movies_grid_view);
        gView.setAdapter(movieDetailsAdapter);

        gView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                MovieDetailResults.MovieDetail movie = movieDetailsAdapter.getItem(i);
                Intent movieDetailIntent = new Intent(getContext(),DisplayMovieDetailsActivity.class);
                movieDetailIntent.putExtra(getString(R.string.movie_details_data), (Parcelable) movie);
                startActivity(movieDetailIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        gView.setAdapter(movieDetailsAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String sortOrder = MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " ASC";
        Uri movieList = MovieContract.MovieEntry.CONTENT_URI;

        return new CursorLoader(getContext(),
                movieList,
                LIST_COLUMNS,
                MovieContract.MovieEntry.COLUMN_MOVIE_LIST_TYPE,
                new String[]{mlistType},
                sortOrder);
    }
}
