package com.example.jessemitchell.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jessemitchell.popularmovies.app.data.MovieDetailResults;

public class MainActivity extends AppCompatActivity implements PopularMoviesFragment.Callback {

    String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.moive_detail_container) != null)
        {
            mTwoPane = true;

            if (savedInstanceState == null)
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.moive_detail_container,
                        new MovieDetailFragment(),
                        DETAILFRAGMENT_TAG).commit();
        }
        else
        {
            mTwoPane = false;
        }

    }

    @Override
    public void onItemSelected(MovieDetailResults.MovieDetail movie)
    {
        if (mTwoPane)
        {
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.MOVIE_DETAIL_URI, movie);
            MovieDetailFragment frag = new MovieDetailFragment();
            frag.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.moive_detail_container, frag, DETAILFRAGMENT_TAG).commit();
        }
        else
        {
                Intent movieDetailIntent = new Intent(this,DisplayMovieDetailsActivity.class);
                movieDetailIntent.putExtra(getString(R.string.movie_details_data), (Parcelable) movie);
                startActivity(movieDetailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.title_activity_settings)
        {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "on pause");
    }

    @Override
    protected void onStop() {

        Log.v(LOG_TAG, "on stop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.v(LOG_TAG, "on resume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.v(LOG_TAG, "on start");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "on destroy");
        super.onDestroy();
    }
}
