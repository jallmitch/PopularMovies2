package com.example.jessemitchell.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessemitchell.popularmovies.app.data.MovieDetailResults;
import com.example.jessemitchell.popularmovies.app.data.NetworkService;
import com.example.jessemitchell.popularmovies.app.data.ReviewDetailResults;
import com.example.jessemitchell.popularmovies.app.data.VideoDetailResults;
import com.example.jessemitchell.popularmovies.app.presentors.MovieDetailsInteractor;
import com.example.jessemitchell.popularmovies.app.presentors.MovieDetailsPresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jesse.mitchell on 1/19/2017.
 */

public class MovieDetailFragment extends Fragment
{
    private final String LOG_TAG_FRAG = MovieDetailFragment.class.getSimpleName();

    private List<String> groupHeaders;
    private HashMap<String, List<String>> listChildren;
    private ExpandableListAdapter exListAdapter;
    private NetworkService service;
    private MovieDetailsInteractor detailsInteractor;

    private MovieDetailResults.MovieDetail movie;

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        String movieDetailData = getString(R.string.movie_details_data);
        View rootView = inflater.inflate(R.layout.movie_detail_main,container,false);

        service = new RxApplication().getNetorkService();


        if(intent != null && intent.hasExtra(movieDetailData))
        {
            movie = intent.getParcelableExtra(movieDetailData);


            CheckBox chBox = (CheckBox)rootView.findViewById(R.id.checkbox_favorite);
            chBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
//                        addToFavorites();
                    Toast.makeText(getContext(),"You have selected this movie as your favorite", Toast.LENGTH_SHORT).show();
                }
            });

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

            List<String> crap = new ArrayList<>();
            ExpandableListView exListView = (ExpandableListView) rootView.findViewById(R.id.detail_expand_view);
            ExpandableListAdapter exListAdapter = new ExpandableListAdapter(getContext(), crap, new HashMap<String, List<String>>());



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

    @Override
    public void onPause() {
        detailsInteractor.unSubscribeMovieReviews();
        super.onPause();
    }


    private void loadData()
    {
        groupHeaders =new ArrayList<>();

        groupHeaders.add(0, "Trailers");
        groupHeaders.add(1, "Reviews");
        listChildren = new HashMap<>();

        detailsInteractor = new MovieDetailsPresenter(this, service, movie.getId());
        detailsInteractor.loadMovieReviews();
//        detailsInteractor.loadMovieTrailers();

    }

    public void loadRetroData(MovieDetailsPresenter.TrailersAndReviews trailersAndReviews)
    {
        List<ReviewDetailResults.ReviewDetail> reviewResults = trailersAndReviews.reviews.getResults();
        List<VideoDetailResults.VideoDetail> trailerResults = trailersAndReviews.trailers.getResults();

        List<String> reviews = new ArrayList<>();
        for(ReviewDetailResults.ReviewDetail rd : reviewResults)
        {
            reviews.add(rd.getContent());
        }
        exListAdapter.setReviewData(reviews);

        List<String> trailers = new ArrayList<>();
        for(VideoDetailResults.VideoDetail vd : trailerResults)
        {
            trailers.add(vd.getName());
        }
        exListAdapter.setTrailerData(trailers);
    }

//    public void loadTrailerData(VideoDetailResults videoDetailResults)
//    {
//        List<VideoDetailResults.VideoDetail> videoResults = videoDetailResults.getResults();
//        List<String> videos = new ArrayList<>();
//
//        for(VideoDetailResults.VideoDetail video : videoResults)
//        {
//            videos.add(video.getName());
//        }
//
//        exListAdapter.setTrailerData(videos);
//    }

}
