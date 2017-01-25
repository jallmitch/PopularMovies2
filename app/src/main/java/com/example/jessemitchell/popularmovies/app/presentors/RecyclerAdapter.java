package com.example.jessemitchell.popularmovies.app.presentors;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jessemitchell.popularmovies.app.PopularMoviesFragment;
import com.example.jessemitchell.popularmovies.app.R;
import com.example.jessemitchell.popularmovies.app.data.MovieDetailResults;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jesse.mitchell on 1/24/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MovieHolder>
{
    public interface OnItemClickListener
    {
        void onItemClick(MovieDetailResults.MovieDetail movie);
    }

    private ArrayList<MovieDetailResults.MovieDetail> mMovies;
    private final OnItemClickListener mListener;

    public RecyclerAdapter(ArrayList<MovieDetailResults.MovieDetail> movies, OnItemClickListener listener)
    {
        mMovies = movies;
        mListener = listener;
    }

    @Override
    public RecyclerAdapter.MovieHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View inflatedView = LayoutInflater.from(parent.getContext())
                                          .inflate(R.layout.movie_recyclerview_item,
                                                   parent,
                                                   false);
        return new MovieHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MovieHolder holder, int position)
    {
        holder.bindMovie(mMovies.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public static class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView mMovieImage;
        private MovieDetailResults.MovieDetail mMovieDetail;

        private static final String MOVIE_KEY = "MOVIE";

        public MovieHolder(View view)
        {
            super(view);
            mMovieImage = (ImageView) view.findViewById(R.id.movie_poster);
            mMovieImage.setAdjustViewBounds(true);
        }

        @Override
        public void onClick(View v)
        {
            Context context = v.getContext();
            Intent showMovieIntent = new Intent(context, PopularMoviesFragment.class);
            showMovieIntent.putExtra(MOVIE_KEY, mMovieDetail);
            context.startActivity(showMovieIntent);
        }

        public void bindMovie(MovieDetailResults.MovieDetail movieDetail, final OnItemClickListener listener)
        {
            mMovieDetail = movieDetail;
            Picasso.with(mMovieImage.getContext())
                                    .load(buildPosterPath(mMovieDetail.getPosterPath()))
                                    .into(mMovieImage);

            mMovieImage.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movieDetail);
                }
            });

        }

        private String buildPosterPath(String imageKey)
        {
            StringBuilder sb = new StringBuilder("http://image.tmdb.org/t/p/")
                    .append("w185")
                    .append(imageKey);

            return sb.toString();
        }
    }

}

