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

//                loadTrailerData();
//                loadReviewData();

            ExpandableListView exListView = (ExpandableListView) rootView.findViewById(R.id.detail_expand_view);
            ExpandableListAdapter exListAdapter = new ExpandableListAdapter(getContext(),  new ArrayList<>(), new HashMap<>());



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
        super.onPause();
    }


    private void loadData()
    {
        groupHeaders =new ArrayList<>();
        listChildren = new HashMap<>();

        detailsInteractor = new MovieDetailsPresenter(this, service, movie.getId());
        detailsInteractor.loadMovieReviews();
        detailsInteractor.loadMovieTrailers();
//
//        groupHeaders.add(0, "Trailers");
//        groupHeaders.add(1, "Reviews");
//
//        List<String> videos = new ArrayList<>();
//        videos.add(0, "Official Trailer #1");
//        videos.add(1, "Team Suicide Squad");
//        videos.add(2, "Harley Quinn Therapy");
//        videos.add(3, "comic con remix trailer");
//        videos.add(4, "Blitz Trailer");

//        List<String> reviews = new ArrayList<>();
//        reviews.add(0, "Summertime 2016 has not been very kind to DC Comics-based personalities looking to shine consistently like their big screen Marvel Comics counterparts. Following the super-sized dud that was _Batman v. Superman: Dawn of Justice_ released a few months ago must really put some major pressure on Warner Bros. to gamble on ensuring that the presence of **Suicide Squad** does not meet the same kind of indifferent reception. Well, it turns out that although the anticipation was high for...(line truncated)...");
//        reviews.add(1, "Suicide Squad is the third and latest entry into the DCEU, and is about a bunch of bad guys that are rounded up to fight for someone else. And just like this year's BvS, this movie received overwhelmingly negative reviews by the critics and was divided among the fans. I was super curious to watch it because unlike many, I actually enjoyed the DCEU till this point. Enjoyed both Man of Steel and BvS. But unfortunately, this one's a mess.\\r\\n\\r\\nThe majority of the movie just fe...(line truncated)... ");
//        reviews.add(2, "Some semi-interesting visuals and a few characters I'd like to get to know, but an absolute mess of a movie. The thing feels like a trailer, or a clipshow, or a music video or some other sort of two-hour long promotional material for the actual _Suicide Squad_ that comes out later.\\r\\n\\r\\n_Final rating:★★ - Had some things that appeal to me, but a poor finished product._");
//        reviews.add(3, "**They are not superheroes, they are supervillains.**\\r\\n\\r\\nIt's nothing against DC, but overall I'm starting to think the todays cinema is getting crowded with the lots of superheroes. Just like any pollution or the over population on the earth's surface. It needs stability, but nobody cares about it other than money making agenda. I also think it's going to last for only a few more years, when this trend going to end like that happened in the 70s, 80s and the 90s. And the ...(line truncated)...");
//
//        listChildren.put(groupHeaders.get(0), videos);
//        listChildren.put(groupHeaders.get(1), reviews);
    }

    private List<String> reviews;
    public void loadReviewData(ReviewDetailResults reviewDetailResults)
    {
        List<ReviewDetailResults.ReviewDetail> reviewResults = reviewDetailResults.getResults();
        reviews = new ArrayList<>();
        for(ReviewDetailResults.ReviewDetail rd : reviewResults)
        {
            reviews.add(rd.getContent());
        }
    }

    private List<String> videos;

    public void loadTrailerData(VideoDetailResults videoDetailResults)
    {
        List<VideoDetailResults.VideoDetail> videoResults = videoDetailResults.getResults();
        videos = new ArrayList<>();

        for(VideoDetailResults.VideoDetail video : videoResults)
        {
            videos.add(video.getName());
        }
    }

//    private void addToFavorites()
//    {
//        final String FAVORITE = "favorite";
//        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
//        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put(MovieContract.MovieEntry._ID, movie.getId());
//        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
//        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
//        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
//        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
//        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
//        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_LIST_TYPE, FAVORITE);
//
//        db.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
//
//        db.close();
//
//        //Todo: save trailers
//        //Todo: save reviews
//    }

}
