package com.example.jessemitchell.popularmovies.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.jessemitchell.popularmovies.app.POJOs.MovieDetails;
import com.example.jessemitchell.popularmovies.app.POJOs.TheMovieDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.jessemitchell.popularmovies.app.BuildConfig.MOVIE_DB_API_KEY;

/**
 * Created by jesse.mitchell on 12/28/2016.
 */
public class PopularMoviesFragment extends Fragment
{

    private final String LOG_TAG = PopularMoviesFragment.class.getSimpleName();

    private ImageAdapter movieDetailsAdapter;

    @Override
    public void onStart() {
        super.onStart();
        selectList();
    }

    private void selectList()
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String listType = sharedPrefs.getString(getString(R.string.pref_list_key),getString(R.string.pref_list_default));

        final String baseUrl = "https://api.themoviedb.org/";
        final String API_KEY_PARM = "api_key";
        final String LANG_PARAM = "language";
        final String LANG = "en-US";
        Map<String, String> params = new HashMap<>();
        params.put(API_KEY_PARM, MOVIE_DB_API_KEY);
        params.put(LANG_PARAM, LANG);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDB movieList = retrofit.create(TheMovieDB.class);

        Call<MovieDetails> movies = movieList.getMovies(listType, params);

        movies.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails body = response.body();
                List<MovieDetails.Result> results = body.getResults();

                movieDetailsAdapter.clear();
                for(MovieDetails.Result movie : results)
                {
                    movieDetailsAdapter.add(movie);
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movies_main, container, false);

        movieDetailsAdapter = new  ImageAdapter(this.getContext(), new ArrayList<MovieDetails.Result>());

        GridView gView = (GridView)rootView.findViewById(R.id.movies_grid_view);
        gView.setAdapter(movieDetailsAdapter);

        gView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieDetails.Result movie = movieDetailsAdapter.getItem(i);
                Intent movieDetailIntent = new Intent(getContext(),DisplayMovieDetailsActivity.class);
                movieDetailIntent.putExtra(getString(R.string.movie_details_data), (Parcelable) movie);
                startActivity(movieDetailIntent);
            }
        });
        return rootView;
    }

    // Used https://github.com/udacity/android-custom-arrayadapter as a guide to builde the adapter
    public class ImageAdapter extends ArrayAdapter<MovieDetails.Result>
    {
        private final String LOG_TAG = ImageAdapter.class.getSimpleName();

        public ImageAdapter(Context context, List<MovieDetails.Result> movieDetails)
        {
            super(context, 0, movieDetails);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MovieDetails.Result details = getItem(position);

            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(new GridView.LayoutParams(740, 1112));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(10, 10, 10, 10);
            } else {
                imageView = (ImageView) convertView;
            }

            Picasso.with(parent.getContext()).load(details.getPosterPath()).into(imageView);
            return imageView;
        }
    }
}
