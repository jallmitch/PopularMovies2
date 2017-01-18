package com.example.jessemitchell.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessemitchell.popularmovies.app.POJOs.MovieDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayMovieDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG_ACT = DisplayMovieDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie_details);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.activity_display_movie_details, new MovieDetailFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.title_activity_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MovieDetailFragment extends Fragment
    {
        private final String LOG_TAG_FRAG = MovieDetailFragment.class.getSimpleName();
        private List<String> groupHeaders;
        private HashMap<String, List<String>> listChildren;


        private MovieDetails.Result movie;
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            Intent intent = getActivity().getIntent();
            String movieDetailData = getString(R.string.movie_details_data);
            View rootView = inflater.inflate(R.layout.movie_detail_main,container,false);


            if(intent != null && intent.hasExtra(movieDetailData))
            {
                movie = intent.getParcelableExtra(movieDetailData);

                // Set Title
                ((TextView)rootView.findViewById(R.id.movie_text_view)).setText(movie.getTitle());

                // Set Voter Average
                ((RatingBar)rootView.findViewById(R.id.vote_average_bar)).setNumStars(5);
                ((RatingBar)rootView.findViewById(R.id.vote_average_bar)).setRating(movie.getVoteAverage().floatValue());

                // Change Image Size and display
                ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_image_view);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(10, 10, 10, 10);
                Picasso.with(getContext()).load(movie.getPosterPath()).into(imageView);

                // Set Release Date
                ((TextView)rootView.findViewById(R.id.date_text_view)).setText(movie.getReleaseDate());

                // Set Overview text.
                // setMovementMethod http://stackoverflow.com/questions/1748977/making-textview-scrollable-in-android
                ((TextView)rootView.findViewById(R.id.overview_text_view)).setText(movie.getOverview());
                ((TextView)rootView.findViewById(R.id.overview_text_view))
                        .setMovementMethod(new ScrollingMovementMethod());

                loadData();

                ExpandableListView exListView = (ExpandableListView) rootView.findViewById(R.id.detail_expand_view);
                ExpandableListAdapter exListAdapter = new ExpandableListAdapter(getContext(), groupHeaders, listChildren);

                exListView.setAdapter(exListAdapter);

                exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
                    {
                        Toast.makeText(getContext(), "Yes you have selected to expand the group", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                exListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {

                        Toast.makeText(getContext(), "Group Should Expand", Toast.LENGTH_SHORT).show();
                    }
                });

                exListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) {

                        Toast.makeText(getContext(), "Group should collapse", Toast.LENGTH_SHORT).show();
                    }
                });

                exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                        Toast.makeText(getContext(), "Yes you have selected to expand the child", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                });
            }
            else
                Log.e(LOG_TAG_FRAG, "Intent was null or data was not sent.");
            return rootView;
        }

        private void loadData()
        {
            groupHeaders =new ArrayList<>();
            listChildren = new HashMap<>();

            groupHeaders.add(0, "Videos");
            groupHeaders.add(1, "Reviews");

            List<String> videos = new ArrayList<>();
            videos.add(0, "Official Trailer #1");
            videos.add(1, "Team Suicide Squad");
            videos.add(2, "Harley Quinn Therapy");
            videos.add(3, "comic con remix trailer");
            videos.add(4, "Blitz Trailer");

            List<String> reviews = new ArrayList<>();
            reviews.add(0, "Summertime 2016 has not been very kind to DC.....");
            reviews.add(1, "Suicide Squad is the third and latest entry......");
            reviews.add(2, "Some semi-interesting visuals and a few..........");
            reviews.add(3, "**They are not superheroes, they are ............");

            listChildren.put(groupHeaders.get(0), videos);
            listChildren.put(groupHeaders.get(1), reviews);
        }

    }
}
