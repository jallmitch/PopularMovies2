package com.example.jessemitchell.popularmovies.app.presentors;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.jessemitchell.popularmovies.app.data.MovieDetailResults;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jesse.mitchell on 1/21/2017.
 */

// Used https://github.com/udacity/android-custom-arrayadapter as a guide to builde the adapter
public class ImageAdapter extends ArrayAdapter<MovieDetailResults.MovieDetail>
{
    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    public ImageAdapter(Context context, List<MovieDetailResults.MovieDetail> movieDetails)
    {
        super(context, 0, movieDetails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieDetailResults.MovieDetail details = getItem(position);

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(
                    new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(10, 10, 10, 10);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(parent.getContext()).load(buildPosterPath(details.getPosterPath())).into(imageView);
        return imageView;
    }

    private String buildPosterPath(String imageKey)
    {
        StringBuilder sb = new StringBuilder("http://image.tmdb.org/t/p/")
                .append("w185")
                .append(imageKey);

        return sb.toString();
    }
}
