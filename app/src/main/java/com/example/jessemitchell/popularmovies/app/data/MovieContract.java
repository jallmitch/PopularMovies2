package com.example.jessemitchell.popularmovies.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jesse.mitchell on 1/13/2017.
 */

public class MovieContract
{
    public static final String CONTENT_AUTHORITY = "com.example.jessemitchell.popularmovies.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movies";
    public static final String PATH_VIDEO = "videos";
    public static final String PATH_REVIEW = "reviews";

    //    content://authority/movie/ - all movies
    //    content://authority/movie/id - specific movie with information
    public static final class MovieEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                                                              .appendPath(PATH_MOVIE).build();

        // content://authority/movie - all movies
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                                                                  CONTENT_AUTHORITY + "/" +
                                                                  PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                                                                       CONTENT_AUTHORITY + "/" +
                                                                       PATH_MOVIE;

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        // Title of the movie
        public static final String COLUMN_MOVIE_TITLE = "title";

        // Brief synopsis of the movie
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        // API returns the date as YYYY-MM-DD
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";

        // API returns the image file name of the poster
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";

        // Returned from the API as a float: Rating is on scale from 1-10
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";

        // Stored as the type of list: POPULAR | TOP_RATED | FAVORITE
        public static final String COLUMN_MOVIE_LIST_TYPE = "list_type";

        public static Uri buildMovieUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieWithVideosUri(long movieId)
        {

            return CONTENT_URI.buildUpon().appendPath(Long.toString(movieId)).appendPath(PATH_VIDEO).build();
        }

        public static String getMovieIdFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }
    }

    //    content://authority/movie/id/video - all videos for a particular movie
    //    content://authority/video/id - specific video
    public static final class VideoEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_VIDEO).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" +
                PATH_VIDEO;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" +
                PATH_VIDEO;

        public static final String TABLE_NAME = "videos";

        // API returns UUID string, storing as text
        public static final String COLUMN_VIDEO_ID = "video_id";

        // Foreign Key to tie the videos to the movie
        public static final String COLUMN_MOVIES_KEY = "movies_id";

        // Name of the video
        public static final String COLUMN_VIDEO_NAME = "name";

        // API returns the hosting site of the video
        public static final String COLUMN_VIDEO_SITE = "site";

        // API returns the key for the video, on the hosting site
        public static final String COLUMN_VIDEO_SITE_KEY = "site_key";

        // API returns the size of the video: 720 | 1080
        public static final String COLUMN_VIDEO_SIZE = "size";

        // API returns the type of the video: Trailer | Featurette
        public static final String COLUMN_VIDEO_TYPE = "type";

        public static Uri buildVideoUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    //      content://authority/movie/id/reviews - all reviews for a particular movie
//    content://authority/review/id - specific review.
    public static final class ReviewEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                                                              .appendPath(PATH_REVIEW)
                                                              .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                                                               + CONTENT_AUTHORITY + "/"
                                                               + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                                                                    + CONTENT_AUTHORITY + "/"
                                                                    + PATH_REVIEW;

        public static final String TABLE_NAME = "reviews";

        // Foreign key to tie the review to the movie
        public static final String COLUMN_MOVIES_KEY = "movie_id";

        // API returns a UUID string, storing as text
        public static final String COLUMN_REVIEW_ID = "review_id";

        // API returns the author name as a string
        public static final String COLUMN_REVIEW_AUTHOR = "review_author";

        // API returns the review with escaped characters, store as string
        public static final String COLUMN_REVIEW_CONTENT = "review_content";

        public static Uri buildReviewUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
