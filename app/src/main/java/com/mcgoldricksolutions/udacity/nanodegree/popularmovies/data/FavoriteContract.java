package com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by dirtbag on 6/30/16.
 */
public class FavoriteContract {

    public static final class FavoriteEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorite";

        // Movie id as returned by API, to identify a particular movie
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // Movie poster url
        public static final String COLUMN_POSTER_URL = "poster_url";




    }
}
