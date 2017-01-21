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
}
