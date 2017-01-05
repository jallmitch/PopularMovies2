package com.example.jessemitchell.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PopularMoviesFragment()).commit();
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
