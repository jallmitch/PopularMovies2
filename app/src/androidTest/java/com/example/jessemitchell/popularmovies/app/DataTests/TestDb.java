package com.example.jessemitchell.popularmovies.app.DataTests;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.jessemitchell.popularmovies.app.data.MovieContract;
import com.example.jessemitchell.popularmovies.app.data.MovieDbHelper;

import java.util.HashSet;

/**
 * Created by jesse.mitchell on 1/13/2017.
 */

public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteDb() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    public void setUp()
    {
        deleteDb();
    }

    public void testCreateDb() throws Throwable
    {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);

        // Creating the connection to the DB.
        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
//
//        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//
//        assertTrue("Error: This means that the database has not been created correctly",
//                cursor.moveToFirst());
//
//        do {
//            tableNameHashSet.isEmpty();
//        } while(cursor.moveToNext());
//
//        checkTables(db, MovieContract.MovieEntry.TABLE_NAME);
//
//
//        TestUtilities.buildMovieTable(mContext);
//        TestUtilities.buildVideoTable(mContext);
//
//        db.close();
//
//        String movidId = Long.toString(TestUtilities.MOVIE_KEY);
//
//        Cursor allMovies = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, MovieContract.MovieEntry._ID, new String[]{movidId}, null);
//
//        Cursor oneMovie = mContext.getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(TestUtilities.MOVIE_KEY), null, null, null, null);
//
//        Cursor oneMovieVideos = mContext.getContentResolver().query(MovieContract.MovieEntry.buildMovieWithVideosUri(TestUtilities.MOVIE_KEY), null,  MovieContract.MovieEntry._ID, new String[]{movidId}, null);

    }

    private void checkTables(SQLiteDatabase db, String tableName)
    {
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                cursor.moveToFirst());

    }
}
