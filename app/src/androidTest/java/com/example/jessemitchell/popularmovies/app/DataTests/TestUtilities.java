package com.example.jessemitchell.popularmovies.app.DataTests;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.jessemitchell.popularmovies.app.data.MovieContract;
import com.example.jessemitchell.popularmovies.app.data.MovieDbHelper;

/**
 * Created by jesse.mitchell on 1/13/2017.
 */

public class TestUtilities extends AndroidTestCase
{
    // Movie Values
    public static final String MOVIE_TITLE = "The Secret Life of Pets";
    public static final String MOVIE_OVERVIEW = "The quiet life of a terrier named Max is upended when his owner takes in Duke, a stray whom Max instantly dislikes.";
    public static final String MOVIE_RELEASE_DATE = "2016-06-18";
    public static final String MOVIE_POSTER_PATH = "/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg";
    public static final float MOVIE_VOTE_AVERAGE = 5.8f;
    public static final String MOVIE_LIST_TYPE = "FAVORITE";
    public static  final int MOVIE_KEY = 328111;

    // Video Values
    public static  final String VIDEO_ID = "";
    public static  final String VIDEO_NAME = "";
    public static  final String VIDEO_SITE = "";
    public static  final String VIDEO_KEY = "";
    public static  final String VIDEO_TYPE = "";
    public static  final String VIDEO_SIZE = "";

    public static void buildMovieTable(Context context)
    {
        SQLiteDatabase db = new MovieDbHelper(context).getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry._ID, MOVIE_KEY);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, MOVIE_TITLE);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE__OVERVIEW, MOVIE_OVERVIEW);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, MOVIE_RELEASE_DATE);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, MOVIE_POSTER_PATH);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, MOVIE_VOTE_AVERAGE);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_LIST_TYPE, MOVIE_LIST_TYPE);

        db.insert(MovieContract.MovieEntry.TABLE_NAME,null,cv);
        db.close();
    }

    public static void buildVideoTable(Context context)
    {
        SQLiteDatabase db = new MovieDbHelper(context).getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MovieContract.VideoEntry.COLUMN_MOVIE_KEY, MOVIE_KEY);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_ID, VIDEO_ID);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_NAME, VIDEO_NAME);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_SITE, VIDEO_SITE);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_SIZE, VIDEO_SIZE);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_SITE_KEY, VIDEO_KEY);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_TYPE, VIDEO_TYPE);

        db.insert(MovieContract.VideoEntry.TABLE_NAME,null,cv);
        db.close();
    }
}
