package com.example.jessemitchell.popularmovies.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jesse.mitchell on 1/13/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper
{
    static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDb)
    {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_LIST_TYPE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL";


        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + MovieContract.VideoEntry.TABLE_NAME +
                  " (" +
                MovieContract.VideoEntry._ID + " TEXT PRIMARY KEY, " +
                MovieContract.VideoEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +
                MovieContract.VideoEntry.COLUMN_VIDEO_KEY + " TEXT NOT NULL, " +
                MovieContract.VideoEntry.COLUMN_VIDEO_NAME + " TEXT NOT NULL, " +
                MovieContract.VideoEntry.COLUMN_VIDEO_SITE + " TEXT NOT NULL, " +
                MovieContract.VideoEntry.COLUMN_VIDEO_SIZE + " INTEGER NOT NULL, " +
                MovieContract.VideoEntry.COLUMN_VIDEO_TYPE + " TEXT NOT NULL " +

                "FOREIGN KEY (" + MovieContract.VideoEntry._ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ")";

        sqlDb.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqlDb.execSQL(SQL_CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
