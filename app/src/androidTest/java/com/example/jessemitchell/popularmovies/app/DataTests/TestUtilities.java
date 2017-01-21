package com.example.jessemitchell.popularmovies.app.DataTests;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.jessemitchell.popularmovies.app.data.MovieContract;

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

}
