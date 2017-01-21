package com.example.jessemitchell.popularmovies.app.presentors;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jessemitchell.popularmovies.app.R;

/**
 * Created by jesse.mitchell on 1/21/2017.
 */

public class MovieDetailsViewHolder
{
    public final TextView movieTitleTextView;
    public final RatingBar voteAverageRatingsBar;
    public final ImageView moviePosterImageView;
    public final TextView releaseDateTextView;
    public final TextView overviewTextView;
    public final ExpandableListView exListView;

    public MovieDetailsViewHolder(View view)
    {
        movieTitleTextView = (TextView)view.findViewById(R.id.movie_text_view);
        voteAverageRatingsBar = (RatingBar)view.findViewById(R.id.vote_average_bar);
        moviePosterImageView = (ImageView)view.findViewById(R.id.movie_image_view);
        releaseDateTextView = (TextView)view.findViewById(R.id.date_text_view);
        overviewTextView = (TextView)view.findViewById(R.id.overview_text_view);
        exListView = (ExpandableListView) view.findViewById(R.id.detail_expand_view);
    }
}
