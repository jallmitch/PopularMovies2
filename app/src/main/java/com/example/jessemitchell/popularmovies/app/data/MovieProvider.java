package com.example.jessemitchell.popularmovies.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by jesse.mitchell on 1/13/2017.
 */

public class MovieProvider extends ContentProvider
{
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDb;

    static final int MOVIES = 100;
    static final int MOVIE_WITH_DETAILS = 101;
    static final int VIDEO = 200;

    @Override
    public boolean onCreate()
    {
        mMovieDb = new MovieDbHelper(getContext());
        return false;
    }

    static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;


        // content://authority/movie - all movies
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIES);
        // content://authority/movie/id - specific movie with information
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_DETAILS);
//        // content://authority/movie/id/video - all videos for a particular movie
//        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*" + MovieContract.PATH_VIDEO, MOVIE_WITH_VIDEOS);
        // content://authority/video/id - specific video
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/*", VIDEO);

        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db = mMovieDb.getReadableDatabase();
        int matchedUri = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (matchedUri)
        {
            case MOVIES:
            {
                break;
            }
            case VIDEO:
            {
                break;
            }
            case MOVIE_WITH_DETAILS:
            {
                break;
            }
            default:
        }


        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        final SQLiteDatabase db = mMovieDb.getWritableDatabase();
        final int uriMatch = sUriMatcher.match(uri);

        Uri returnUri;

        switch (uriMatch)
        {
            case MOVIES:
            {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case VIDEO:
            {
                long _id = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.VideoEntry.buildVideoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mMovieDb.getWritableDatabase();
        final int uriMatch = sUriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (uriMatch)
        {
            case MOVIES:
            {
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case VIDEO:
            {
                rowsUpdated = db.update(MovieContract.VideoEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mMovieDb.getWritableDatabase();
        final int uriMatch = sUriMatcher.match(uri);
        int deletedRow = 0;

        switch (uriMatch)
        {
            case MOVIES:
            {
                deletedRow = db.delete(MovieContract.MovieEntry.TABLE_NAME,selection, selectionArgs);
                break;
            }
            case VIDEO:
            {
                deletedRow = db.delete(MovieContract.VideoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }

        if (deletedRow != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return deletedRow;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }

    @Override
    public void shutdown()
    {
        mMovieDb.close();
        super.shutdown();
    }
}
