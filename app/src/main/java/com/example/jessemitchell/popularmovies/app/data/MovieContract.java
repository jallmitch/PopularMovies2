package com.example.jessemitchell.popularmovies.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jesse.mitchell on 1/12/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.jessemitchell.popularmovies.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movies";
    public static final String PATH_VIDEO = "videos";

    public static final class MovieEntry implements BaseColumns
    {
        //content://authority/movie - all movies
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                                                          .appendPath(PATH_MOVIE).build();

        // Define contract entry content types
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                                                 + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                                                 + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Define table and columns

        public static final String TABLE_NAME = "movies";

        // Field setting the type of list of movies: POPULAR, TOP_RATED, FAVORITES
        public static final String COLUMN_MOVIE_LIST_TYPE = "movie_list_type";

        // The movie title
        public static final String COLUMN_MOVIE_TITLE = "movie_title";

        // A brief description of the movie
        public static final String COLUMN_MOVIE_OVERVIEW = "movie_orview";

        // Release date of the movie stored as a date: "YYYY-MM-DD"
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";

        // The image file name of the poster, representing the movie: "/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg"
        public static final String COLUMN_MOVIE_POSTER_PATH = "movie_post_path";

        // Stored as a float. The scale is based from 1-10: example 5.8
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average";

        // content://authority/movie/id - specific movie with information
        public static Uri buildMovieUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        //content://authority/movie/id/video - all videos for a particular movie
        public static Uri buildMovieWithVideo(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id).withAppendedPath(CONTENT_URI, PATH_VIDEO);
        }
    }

    public static final class VideoEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_VIDEO).build();

        // Define contract entry content types
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                                                     + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                                                     + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static final String TABLE_NAME = "videos";

        // Foreign key from within the "favorites" table
        public static final String COLUMN_MOVIE_KEY = "movie_id";

        // Title or name given to the video
        public static final String COLUMN_VIDEO_NAME = "video_name";

        // Website that hosts the video
        public static final String COLUMN_VIDEO_SITE = "video_site";

        // Website's key for the video stored as a String: "SUXWAEX2jlg"
        public static final String COLUMN_VIDEO_KEY = "video_key";

        // Description of the type of video
        public static final String COLUMN_VIDEO_TYPE = "video_type";

        // Output size of the video stored as an int: 720, 1080 etc..
        public static final String COLUMN_VIDEO_SIZE = "video_size";


        // content://authority/video/id - specific video
        public static Uri buildVideoUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
