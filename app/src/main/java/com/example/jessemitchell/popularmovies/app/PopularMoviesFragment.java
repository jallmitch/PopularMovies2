package com.example.jessemitchell.popularmovies.app;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jessemitchell.popularmovies.app.data.MovieContract;
import com.example.jessemitchell.popularmovies.app.data.MovieDetailResults;
import com.example.jessemitchell.popularmovies.app.presentors.MovieListInteractor;
import com.example.jessemitchell.popularmovies.app.presentors.MovieListPresenter;
import com.example.jessemitchell.popularmovies.app.presentors.RecyclerAdapter;

import java.util.ArrayList;

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
    private final String LAYOUT_MANAGER_KEY = "layoutmanager";
    private final String KEY_RECYCLER_STATE = "movieRecycler";
    private final String TAG = "RecyclerViewFragment";

    private String mlistType;
    private MovieListInteractor moviList;
    private ArrayList<MovieDetailResults.MovieDetail> mMovies;
    private RecyclerAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private Parcelable stateParcelable;
    private Bundle mBundleState;
    int currentPosition = 0;

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
        super.onStart();
        if (mMovies.size() == 0)
            moviList.loadMovieList();;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        stateParcelable = mLinearLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLER_STATE, stateParcelable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null ) {
            stateParcelable = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            mLinearLayoutManager.onRestoreInstanceState(stateParcelable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

        if (!mlistType.equals("favorites"))
            moviList.unSubscribeMovieList();

    }

    public interface Callback
    {
        void onItemSelected(MovieDetailResults.MovieDetail movie);
    }

    public void addMovies(MovieDetailResults movies)
    {
        mMovies.addAll(movies.getResults());
        mMovieAdapter.notifyItemInserted(mMovies.size());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mlistType = sharedPrefs.getString(getString(R.string.pref_list_key),getString(R.string.pref_list_default));
        moviList = new MovieListPresenter(this, mlistType);
        mMovies = new ArrayList<>();
        getLoaderManager().initLoader(LIST_LOADER, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movies_main, container, false);
        rootView.setTag(TAG);


        if (rootView.findViewById(R.id.recyclerViewDualPayne) != null)
        {
            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerViewDualPayne);
            mLinearLayoutManager = new GridLayoutManager(getContext(), 1);
        }
        else
        {
            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
            mLinearLayoutManager = new GridLayoutManager(getContext(), 3);
        }

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMovieAdapter = new RecyclerAdapter(mMovies, new RecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(MovieDetailResults.MovieDetail movie) {
                ((Callback) getActivity()).onItemSelected(movie);
            }
        });
        mRecyclerView.setAdapter(mMovieAdapter);
        return rootView;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mRecyclerView.setAdapter(mMovieAdapter);
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
