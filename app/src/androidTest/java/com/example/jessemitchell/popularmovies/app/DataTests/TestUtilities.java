package com.example.jessemitchell.popularmovies.app.DataTests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.jessemitchell.popularmovies.app.data.MovieContract;
import com.example.jessemitchell.popularmovies.app.data.MovieDbHelper;

import java.util.Map;
import java.util.Set;

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
    public static  final long MOVIE_KEY = 328111;

    // Video Values

    public static  final String VIDEO_ID = "571cdc48c3a3684e620018b8";
    public static  final String VIDEO_NAME = "Official Teaser Trailer";
    public static  final String VIDEO_SITE = "YouTube";
    public static  final String VIDEO_KEY = "i-80SGWfEjM";
    public static  final String VIDEO_TYPE = "Trailer";
    public static  final int VIDEO_SIZE = 1080;

    public static void buildMovieTable(Context context)
    {
        SQLiteDatabase db = new MovieDbHelper(context).getWritableDatabase();

        ContentValues cv = buildMovieValues();

        db.insert(MovieContract.MovieEntry.TABLE_NAME,null,cv);
        db.close();
    }

    public static void buildVideoTable(Context context)
    {
        SQLiteDatabase db = new MovieDbHelper(context).getWritableDatabase();

        ContentValues cv = buildMovieTrailerValues();

        db.insert(MovieContract.VideoEntry.TABLE_NAME,null,cv);
        db.close();
    }

    public static ContentValues buildMovieValues()
    {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry._ID, MOVIE_KEY);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, MOVIE_TITLE);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, MOVIE_OVERVIEW);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, MOVIE_RELEASE_DATE);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, MOVIE_POSTER_PATH);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, MOVIE_VOTE_AVERAGE);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_LIST_TYPE, MOVIE_LIST_TYPE);

        return cv;
    }

    public static ContentValues buildMovieTrailerValues()
    {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.VideoEntry.COLUMN_MOVIES_KEY, MOVIE_KEY);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_ID, VIDEO_ID);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_NAME, VIDEO_NAME);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_SITE, VIDEO_SITE);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_SIZE, VIDEO_SIZE);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_SITE_KEY, VIDEO_KEY);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_TYPE, VIDEO_TYPE);

        return cv;
    }

    public static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            String actualValue = valueCursor.getString(idx);
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, actualValue);
        }
    }

    public static void validateJoinCursor(String error, Cursor valueCursor, ContentValues movieValues, ContentValues videoValues)
    {
        Set<Map.Entry<String, Object>> movieSet = movieValues.valueSet();

        for (Map.Entry<String, Object> entry : movieSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            String actualValue = valueCursor.getString(idx);
            if (!columnName.equals("_id"))
                assertEquals("Value '" + entry.getValue().toString() +
                        "' did not match the expected value '" +
                        expectedValue + "'. " + error, expectedValue, actualValue);
        }
        Set<Map.Entry<String, Object>> videoSet = videoValues.valueSet();

        for (Map.Entry<String, Object> entry : videoSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            String actualValue = valueCursor.getString(idx);
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, actualValue);
        }
    }
}
