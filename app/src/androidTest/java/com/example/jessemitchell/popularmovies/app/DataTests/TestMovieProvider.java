package com.example.jessemitchell.popularmovies.app.DataTests;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
    }

    public void testBasicMovieCrudOperations()
    {
        MovieDbHelper movieDbHelper = new MovieDbHelper(mContext);
        String tableName = MovieContract.MovieEntry.TABLE_NAME;
            // Remove previous data
        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);

        ContentValues content = TestUtilities.buildMovieValues();
        long movieId = insertEntryData(movieDbHelper, tableName, content);

        Cursor movieCursor = getCursor(MovieContract.MovieEntry.CONTENT_URI);

        assertTrue("Query Failed to return a single result", movieCursor.moveToFirst());
        TestUtilities.validateCursor("Movie query failed", movieCursor, content);

        movieCursor.close();
        assertTrue(movieCursor.isClosed());

        ContentValues updateValues = new ContentValues();
        updateValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_LIST_TYPE, "POPULAR");
        updateEntryData(movieDbHelper,tableName,updateValues,"_id=?", new String[]{Long.toString(TestUtilities.MOVIE_KEY)});

        Cursor updateCursor = getCursor(MovieContract.MovieEntry.CONTENT_URI);

        assertTrue("Query Failed to return a single result", updateCursor.moveToFirst());

        int listTypeIndex = updateCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_LIST_TYPE);
        String listType = updateCursor.getString(listTypeIndex);
        assertFalse("Values should not match", listType.equals(TestUtilities.MOVIE_LIST_TYPE));

        updateCursor.close();
        assertTrue(updateCursor.isClosed());

        assertTrue("Record was not deleted with id:" + movieId, deleteEntryData(movieDbHelper,tableName, movieId) == 1);


    }



    public void testQueryMethod()
    {
        MovieDbHelper movieDbHelper = new MovieDbHelper(mContext);
        String movieTableName = MovieContract.MovieEntry.TABLE_NAME;

        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);

        ContentValues movieContent = TestUtilities.buildMovieValues();
        long movieId = insertEntryData(movieDbHelper, movieTableName, movieContent);

    }

    private Cursor getCursor(Uri uri) {
        return mContext.getContentResolver().query(
                    uri,
                    null,
                    null,
                    null,
                    null);
    }

    private long insertEntryData(MovieDbHelper helper, String tableName, ContentValues content)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        long movieId = db.insert(tableName, null, content);
        assertTrue("Failed to insert Movie Detail record", movieId != -1);

        db.close();

        return movieId;
    }

    private long updateEntryData(MovieDbHelper helper, String tableName, ContentValues content, String clause, String[] selectionArgs)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        long movieId = db.update(tableName, content, clause, selectionArgs);
        assertTrue("Failed to insert Movie Detail record", movieId != -1);

        db.close();

        return movieId;
    }

    private long deleteEntryData(MovieDbHelper helper, String tableName, long id)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        long movieId = db.delete(tableName, "_id=?", new String[]{Long.toString(id)});
        assertTrue("Failed to insert Movie Detail record", movieId != -1);

        db.close();
        return movieId;
    }

}
