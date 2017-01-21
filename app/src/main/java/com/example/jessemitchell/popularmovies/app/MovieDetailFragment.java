package com.example.jessemitchell.popularmovies.app;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

import com.example.jessemitchell.popularmovies.app.data.MovieContract;
import com.example.jessemitchell.popularmovies.app.data.MovieDbHelper;
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
    private List<ReviewDetailResults.ReviewDetail> reviewResults;
    private List<VideoDetailResults.VideoDetail> trailerResults;


    private final int TRAILERS = 0;
    private final int REVIEWS = 1;

    private MovieDetailResults.MovieDetail movie;

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void addFavorite()
    {
        MovieDbHelper helper = new MovieDbHelper(getContext());
        ContentResolver contentResolver = getContext().getContentResolver();

        ContentValues mCV = getMovieContentValues();
        SQLiteDatabase db = helper.getWritableDatabase();
        long movieId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, mCV);
        db.close();


        int count = 0;
        if (reviewResults.size() > 0) {
            ContentValues[] rCV = new ContentValues[reviewResults.size()];
            for (ReviewDetailResults.ReviewDetail review : reviewResults) {

                rCV[count] = getReviewContentValues(review, movieId);
            }
            contentResolver.bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, rCV);
        }

        if (trailerResults.size() > 0) {
            ContentValues[] vCV = new ContentValues[trailerResults.size()];
            count = 0;
            for (VideoDetailResults.VideoDetail trailer : trailerResults) {

                vCV[count] = getVideoContentValues(trailer, movieId);
            }
            contentResolver.bulkInsert(MovieContract.VideoEntry.CONTENT_URI, vCV);
        }

        verifyData();
    }

    private void verifyData()
    {
        Cursor movieEntries = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        if (movieEntries.moveToFirst())
        {
            int count = movieEntries.getCount();
        }

        movieEntries.close();

        Cursor reviewEntries = getContext().getContentResolver().query(MovieContract.ReviewEntry.CONTENT_URI, null, null, null, null);
        if (reviewEntries.moveToFirst())
        {
            int count = reviewEntries.getCount();
        }
        reviewEntries.close();


        Cursor videoEntries = getContext().getContentResolver().query(MovieContract.VideoEntry.CONTENT_URI, null, null, null, null);
        if (videoEntries.moveToFirst())
        {
            int count = videoEntries.getCount();
        }
        videoEntries.close();
    }

    private ContentValues getMovieContentValues()
    {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_LIST_TYPE, "FAVORITE");
        return cv;
    }

    private ContentValues getVideoContentValues(VideoDetailResults.VideoDetail video, long movieId)
    {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.VideoEntry.COLUMN_MOVIES_KEY, movieId);
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_ID, video.getId());
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_NAME, video.getName());
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_SITE, video.getSite());
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_SITE_KEY, video.getKey());
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_SIZE, video.getSize());
        cv.put(MovieContract.VideoEntry.COLUMN_VIDEO_TYPE, video.getType());
        return cv;
    }

    private ContentValues getReviewContentValues(ReviewDetailResults.ReviewDetail review, long movieId)
    {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.ReviewEntry.COLUMN_MOVIES_KEY, movieId);
        cv.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, review.getId());
        cv.put(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, review.getAuthor());
        cv.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, review.getContent());
        return cv;
    }

    private void removeFavorite()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        String movieDetailData = getString(R.string.movie_details_data);
        View rootView = inflater.inflate(R.layout.movie_detail_main,container,false);
        PackageManager packageManager = getActivity().getPackageManager();
        service = new RxApplication().getNetorkService();


        if(intent != null && intent.hasExtra(movieDetailData))
        {
            movie = intent.getParcelableExtra(movieDetailData);


            CheckBox chBox = (CheckBox)rootView.findViewById(R.id.checkbox_favorite);
            chBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (chBox.isChecked())
                        addFavorite();
                    else
                        removeFavorite();
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

            ExpandableListView exListView = (ExpandableListView) rootView.findViewById(R.id.detail_expand_view);
            exListAdapter = new ExpandableListAdapter(getContext(),  new ArrayList<>(), new HashMap<String, List<String>>());


            exListView.setAdapter(exListAdapter);

            exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
                {
                    return false;
                }
            });

            exListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {

                }
            });

            exListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {

                }
            });

            exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    switch (groupPosition)
                    {
                        case TRAILERS:
                        {
                            String key = trailerResults.get(childPosition).getKey();
                            Uri trailerUri = new Uri.Builder().scheme("https")
                                                              .authority("www.youtube.com")
                                                              .appendPath("watch")
                                                              .appendQueryParameter("v", key)
                                                              .build();
                            Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
                            List<ResolveInfo> intentList = packageManager.queryIntentActivities(trailerIntent, PackageManager.MATCH_DEFAULT_ONLY);
                            if (intentList.size() > 0) {
                                trailerIntent.putExtra("force_fullscreen", true);
                                startActivity(trailerIntent);
                            }
                            else
                            {
                                Toast.makeText(getContext(), "No available player is available for this trailer.",Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case REVIEWS:
                        {
                            break;
                        }
                    }
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
    }

    public void loadRetroData(MovieDetailsPresenter.TrailersAndReviews trailersAndReviews)
    {
        reviewResults = trailersAndReviews.reviews.getResults();
        trailerResults = trailersAndReviews.trailers.getResults();

        List<String> reviews = new ArrayList<>();
        for(ReviewDetailResults.ReviewDetail rd : reviewResults)
        {
            reviews.add(rd.getContent());
        }

        List<String> trailers = new ArrayList<>();
        for(VideoDetailResults.VideoDetail vd : trailerResults)
        {
            trailers.add(vd.getName());
        }

        exListAdapter.setReviewData(reviews);
        exListAdapter.setTrailerData(trailers);
    }

}
