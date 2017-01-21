package com.example.jessemitchell.popularmovies.app.presentors;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by jesse.mitchell on 1/20/2017.
 */

public class MovieDetailFragmentAdapter extends CursorAdapter
{

    public MovieDetailFragmentAdapter(Context context, Cursor cursor, int flags)
    {
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

}
