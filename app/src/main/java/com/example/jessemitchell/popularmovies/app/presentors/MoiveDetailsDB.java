package com.example.jessemitchell.popularmovies.app.presentors;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * Created by jesse.mitchell on 1/20/2017.
 */

public class MoiveDetailsDB implements LoaderManager.LoaderCallbacks<Cursor>
{
    private Uri mUri;
    private Activity mActivity;
    private String[] COLUMNS;

    public MoiveDetailsDB(Activity activity, Uri uri, String[] columns)
    {
        this.COLUMNS = columns;
        this.mActivity = activity;
        this.mUri = uri;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null)
        {
            return new CursorLoader(mActivity, mUri, COLUMNS, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }
}
