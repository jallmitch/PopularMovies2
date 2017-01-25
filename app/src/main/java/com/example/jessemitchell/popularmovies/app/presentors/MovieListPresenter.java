package com.example.jessemitchell.popularmovies.app.presentors;

import android.database.Cursor;
import android.net.Uri;

import com.example.jessemitchell.popularmovies.app.PopularMoviesFragment;
import com.example.jessemitchell.popularmovies.app.data.MovieContract;
import com.example.jessemitchell.popularmovies.app.data.MovieDetailResults;
import com.example.jessemitchell.popularmovies.app.data.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static com.example.jessemitchell.popularmovies.app.BuildConfig.MOVIE_DB_API_KEY;

/**
 * Created by jesse.mitchell on 1/19/2017.
 */

public class MovieListPresenter implements MovieListInteractor
{
    private PopularMoviesFragment fragment;
    private String mListType;
    private NetworkService service;
    private Subscription subscription;

    private static final String[] LIST_COLUMNS =
            {
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                    MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                    MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                    MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE
            };

    private static final int MOVIE_ID = 1;
    private static final int MOVIE_TITLE = 2;
    private static final int MOIVE_OVERVIEW = 3;
    private static final int MOVIE_RELEASE_DATE = 4;
    private static final int MOVIE_POSTER_PATH = 5;
    private static final int MOVIE_VOTE_AVERAGE = 6;

    public MovieListPresenter(PopularMoviesFragment fragment, String listType)
    {
        this.fragment = fragment;
        this.service = new NetworkService();
        this.mListType = listType;
    }

    @Override
    public void setListType(String listType) {
        this.mListType = listType;
    }

    @Override
    public void loadMovieList()
    {
        if(mListType.equals("favorites"))
            pullDataBaseData();
        else
            pullNetworkData();
    }

    private void pullNetworkData()
    {
        final String API_KEY_PARM = "api_key";
        final String LANG_PARAM = "language";
        final String LANG = "en-US";
        Map<String, String> params = new HashMap<>();
        params.put(API_KEY_PARM, MOVIE_DB_API_KEY);
        params.put(LANG_PARAM, LANG);

        Observable<MovieDetailResults> moviesObservable =
                (Observable<MovieDetailResults>)service
                        .getPreparedObservable(service.getAPI()
                                        .getMovies(mListType, params)
                                ,MovieDetailResults.class, true, true );
        subscription = moviesObservable.subscribe(new Observer<MovieDetailResults>() {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(MovieDetailResults movieDetailResults)
            {
                fragment.addMovies(movieDetailResults);
            }
        });
    }

    private void pullDataBaseData()
    {
        Uri movies = MovieContract.MovieEntry.CONTENT_URI;
        Cursor cursor = fragment.getContext().getContentResolver().query(movies,
                LIST_COLUMNS,
                MovieContract.MovieEntry.COLUMN_MOVIE_LIST_TYPE,
                new String[]{"FAVORITE"},
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " ASC");

        List<MovieDetailResults.MovieDetail> movieList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())
        {
            do{

                MovieDetailResults.MovieDetail movieDetail = new MovieDetailResults.MovieDetail();

                movieDetail.setId(cursor.getInt(MOVIE_ID));
                movieDetail.setTitle(cursor.getString(MOVIE_TITLE));
                movieDetail.setOverview(cursor.getString(MOIVE_OVERVIEW));
                movieDetail.setReleaseDate(cursor.getString(MOVIE_RELEASE_DATE));
                movieDetail.setPosterPath(cursor.getString(MOVIE_POSTER_PATH));
                movieDetail.setVoteAverage(cursor.getDouble(MOVIE_VOTE_AVERAGE));

                // Dummy data for Parcable
                movieDetail.setAdult(false);
                movieDetail.setGenreIds(new ArrayList<>());
                movieDetail.setOriginalTitle("");
                movieDetail.setOriginalLanguage("");
                movieDetail.setBackdropPath("");
                movieDetail.setPopularity(0.0);
                movieDetail.setVoteCount(0);
                movieDetail.setVideo(false);

                movieList.add(movieDetail);

            }while(cursor.moveToNext());
        }

        MovieDetailResults movieResults = new MovieDetailResults();
        movieResults.setResults(movieList);
        fragment.addMovies(movieResults);
    }

    @Override
    public void unSubscribeMovieList()
    {
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
