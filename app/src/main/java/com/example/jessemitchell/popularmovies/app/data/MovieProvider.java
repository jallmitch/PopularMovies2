package com.example.jessemitchell.popularmovies.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by jesse.mitchell on 1/13/2017.
 */

public class MovieProvider extends ContentProvider
{
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sMovieJoinVideoQB;
    private static final SQLiteQueryBuilder sMovieJoinReviewQB;
    private MovieDbHelper mMovieDb;

    static final int MOVIES = 100;
    static final int MOVIE_ID = 101;

    static final int VIDEOS = 200;
    static final int VIDEO_ID = 201;
    static final int MOVIE_WITH_VIDEOS = 202;

    static final int REVIEWS = 300;
    static final int REVIEW_ID = 301;
    static final int MOVIE_WITH_REVIEWS = 302;

    static{
        sMovieJoinVideoQB = new SQLiteQueryBuilder();
        sMovieJoinVideoQB.setTables(
                MovieContract.MovieEntry.TABLE_NAME +  " INNER JOIN " +
                        MovieContract.VideoEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        "=" + MovieContract.VideoEntry.TABLE_NAME +
                        "." + MovieContract.VideoEntry.COLUMN_MOVIES_KEY
        );

        sMovieJoinReviewQB = new SQLiteQueryBuilder();
        sMovieJoinReviewQB.setTables(
                MovieContract.MovieEntry.TABLE_NAME +  " INNER JOIN " +
                        MovieContract.ReviewEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        "=" + MovieContract.ReviewEntry.TABLE_NAME +
                        "." + MovieContract.ReviewEntry.COLUMN_MOVIES_KEY
        );
    }

    private Cursor getMoviesWithVideos(Uri uri, String[] projection)
    {
        String movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);
        String selection = MovieContract.VideoEntry.TABLE_NAME +
                "." + MovieContract.VideoEntry.COLUMN_MOVIES_KEY +
                " = ? ";
        String[] selectionArgs = new String[]{movieId};

        return sMovieJoinVideoQB.query(mMovieDb.getReadableDatabase(),
                                       projection,
                                       selection,
                                       selectionArgs,
                                       null,
                                       null,
                                       null);
    }

    private Cursor getMoviesWithReviews(Uri uri, String[] projection)
    {
        String movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);
        String selection = MovieContract.ReviewEntry.TABLE_NAME +
                "." + MovieContract.ReviewEntry.COLUMN_MOVIES_KEY +
                " = ? ";
        String[] selectionArgs = new String[]{movieId};

        return sMovieJoinVideoQB.query(mMovieDb.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

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

        // content://authority/movie/id
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);

        // content://authority/videos
        matcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEOS);

        // content://authority/videos/id
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/#", VIDEO_ID);

        // content://authority/movie/id/videos - trailers associated to a video
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#/" +
                                  MovieContract.PATH_VIDEO, MOVIE_WITH_VIDEOS);

        // content://authority/reviews
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEWS);

        // content://authority/reviews/id
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/#", REVIEW_ID);

        // content://authority/movie/id/reviews - reviews associated to a video
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#/" +
                MovieContract.PATH_REVIEW, MOVIE_WITH_REVIEWS);

        return matcher;
    }

    @Override
    public String getType(Uri uri)
    {
        final int matchUri = sUriMatcher.match(uri);

        switch (matchUri)
        {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case VIDEOS:
                return MovieContract.VideoEntry.CONTENT_TYPE;
            case VIDEO_ID:
                return MovieContract.VideoEntry.CONTENT_ITEM_TYPE;
            case REVIEWS:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case REVIEW_ID:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_VIDEOS:
                return MovieContract.VideoEntry.CONTENT_TYPE;
            case MOVIE_WITH_REVIEWS:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        int matchedUri = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (matchedUri)
        {
            case MOVIES:
            {
                returnCursor = mMovieDb.getReadableDatabase()
                                       .query(MovieContract.MovieEntry.TABLE_NAME,
                                               null,
                                               null,
                                               null,
                                               null,
                                               null,
                                               null);
                break;
            }
            case VIDEOS: {
                returnCursor = mMovieDb.getReadableDatabase()
                        .query(MovieContract.VideoEntry.TABLE_NAME,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);
                break;
            }
            case REVIEWS: {
                returnCursor = mMovieDb.getReadableDatabase()
                        .query(MovieContract.ReviewEntry.TABLE_NAME,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);
                break;
            }
            case MOVIE_ID:
            {
                returnCursor = mMovieDb.getReadableDatabase()
                        .query(MovieContract.MovieEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                null);

                break;
            }
            case MOVIE_WITH_VIDEOS:
            {
                returnCursor = getMoviesWithVideos(uri, projection);
                break;
            }
            case MOVIE_WITH_REVIEWS:
            {
                returnCursor = getMoviesWithReviews(uri, projection);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnCursor;
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
            case VIDEOS:
            {
                long _id = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.VideoEntry.buildVideoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEWS:
            {
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
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
            case VIDEOS:
            {
                rowsUpdated = db.update(MovieContract.VideoEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case REVIEWS:
            {
                rowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME,values,selection,selectionArgs);
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
            case MOVIE_ID:
            {
                deletedRow = db.delete(MovieContract.MovieEntry.TABLE_NAME,selection, selectionArgs);
                break;
            }
            case VIDEOS:
            {
                deletedRow = db.delete(MovieContract.VideoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEWS:
            {
                deletedRow = db.delete(MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
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
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case VIDEOS:
                return doTransactions(uri, MovieContract.VideoEntry.TABLE_NAME, values);

            case REVIEWS:
                return doTransactions(uri, MovieContract.ReviewEntry.TABLE_NAME, values);
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private int doTransactions(Uri uri, String tableName, ContentValues[] values)
    {
        final SQLiteDatabase db = mMovieDb.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;

    }

    @Override
    public void shutdown()
    {
        mMovieDb.close();
        super.shutdown();
    }
}
