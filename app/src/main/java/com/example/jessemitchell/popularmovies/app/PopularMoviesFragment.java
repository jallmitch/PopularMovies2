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

import com.example.jessemitchell.popularmovies.app.POJOs.MovieDetailResults;
import com.example.jessemitchell.popularmovies.app.sync.NetworkService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static com.example.jessemitchell.popularmovies.app.BuildConfig.MOVIE_DB_API_KEY;

/**
 * Created by jesse.mitchell on 12/28/2016.
 *
 * Retrofit resources
 * https://square.github.io/retrofit/
 * https://www.androidtutorialpoint.com/networking/retrofit-android-tutorial/
 */
public class PopularMoviesFragment extends Fragment
{

    private final String LOG_TAG = PopularMoviesFragment.class.getSimpleName();
    private String mlistType;
    private Subscription subscription;

    private ImageAdapter movieDetailsAdapter;

    @Override
    public void onStart()
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mlistType = sharedPrefs.getString(getString(R.string.pref_list_key),getString(R.string.pref_list_default));
        super.onStart();
        selectList();
    }

    private void selectList()
    {

        final String API_KEY_PARM = "api_key";
        final String LANG_PARAM = "language";
        final String LANG = "en-US";
        Map<String, String> params = new HashMap<>();
        params.put(API_KEY_PARM, MOVIE_DB_API_KEY);
        params.put(LANG_PARAM, LANG);

        NetworkService service = new NetworkService();
        Observable<MovieDetailResults> results =
                (Observable<MovieDetailResults>)service
                        .getPreparedObservable(service.getAPI()
                        .getMovies(mlistType, params)
                        ,MovieDetailResults.class, true, true );
        subscription = results.subscribe(new Observer<MovieDetailResults>() {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(MovieDetailResults movieDetailResults)
            {
                List<MovieDetailResults.MovieDetail> mdResults = movieDetailResults.getResults();

                movieDetailsAdapter.clear();

                for (MovieDetailResults.MovieDetail movie : mdResults)
                {
                    movieDetailsAdapter.add(movie);
                }
            }
        });

    }

    public void rxUnSubscribe()
    {
        if(subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        rxUnSubscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movies_main, container, false);
        movieDetailsAdapter = new  ImageAdapter(this.getContext(), new ArrayList<MovieDetailResults.MovieDetail>());

        GridView gView = (GridView)rootView.findViewById(R.id.movies_grid_view);
        gView.setAdapter(movieDetailsAdapter);

        gView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                MovieDetailResults.MovieDetail movie = movieDetailsAdapter.getItem(i);
                Intent movieDetailIntent = new Intent(getContext(),DisplayMovieDetailsActivity.class);
                movieDetailIntent.putExtra(getString(R.string.movie_details_data), (Parcelable) movie);
                startActivity(movieDetailIntent);
            }
        });
        return rootView;
    }

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

            Picasso.with(parent.getContext()).load(details.getPosterPath()).into(imageView);
            return imageView;
        }
    }
}
