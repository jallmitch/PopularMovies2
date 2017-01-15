package com.example.jessemitchell.popularmovies.app.DataTests;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.jessemitchell.popularmovies.app.data.MovieContract;
import com.example.jessemitchell.popularmovies.app.data.MovieDbHelper;
import com.example.jessemitchell.popularmovies.app.data.MovieProvider;

/**
 * Created by jesse.mitchell on 1/14/2017.
 */

public class TestMovieProvider extends AndroidTestCase
{
    public void testProviderRegistry()
    {
        PackageManager pm = mContext.getPackageManager();

        ComponentName compName = new ComponentName(mContext.getPackageName(), MovieProvider.class.getName());

        try
        {
            ProviderInfo pInfo = pm.getProviderInfo(compName,0);
            assertEquals("Error: Authorities do not match: " + pInfo.authority +
                    " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    pInfo.authority,
                    MovieContract.CONTENT_AUTHORITY);
        }
        catch(Exception e)
        {

        }
    }

    public void testGetType()
    {
        // content://authority/movie - all movies
        String type = mContext.getContentResolver().getType(MovieContract.MovieEntry.CONTENT_URI);
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE", MovieContract.MovieEntry.CONTENT_TYPE, type);

        // content://authority/movie/id
        type = mContext.getContentResolver().getType(MovieContract.MovieEntry.buildMovieUri(TestUtilities.MOVIE_KEY));
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE", MovieContract.MovieEntry.CONTENT_ITEM_TYPE, type);

        // content://authority/movie/id/videos - trailers associated to a video
        type = mContext.getContentResolver().getType(MovieContract.MovieEntry.buildMovieWithVideosUri(TestUtilities.MOVIE_KEY));
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE", MovieContract.VideoEntry.CONTENT_TYPE, type);

        // content://authority/video - all videos
        type = mContext.getContentResolver().getType(MovieContract.VideoEntry.CONTENT_URI);
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE", MovieContract.VideoEntry.CONTENT_TYPE, type);

        // content://authority/video/id
        type = mContext.getContentResolver().getType((MovieContract.VideoEntry.buildVideoUri(1)));
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE", MovieContract.VideoEntry.CONTENT_ITEM_TYPE, type);
    }

    public void testBasicMovieQuery()
    {
        // Remove previous data
        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);

        MovieDbHelper helper = new MovieDbHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = TestUtilities.buildMovieValues();

        long movieId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, content);
        assertTrue("Failed to insert Movie Detail record", movieId != -1);

        db.close();

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        TestUtilities.validateCursor("Movie query failed", movieCursor, content);

    }
}
