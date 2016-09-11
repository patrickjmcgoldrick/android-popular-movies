package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;

import com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data.FavoriteContract;

/**
 * Created by dirtbag on 6/23/16.
 */
public class Utility {
    public static final String BASE_URL = "http://api.themoviedb.org/3";
    public static final String API_MOST_POPULAR = "/movie/popular";
    public static final String API_TOP_RATED = "/movie/top_rated";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    public static final String API_PARAMETER = "?api_key=";

    public static final String MOVIE = "/movie/";
    public static final String TRAILERS = "/trailers";
    public static final String REVIEWS = "/reviews";

    public static final String FILTER_TYPE = "filter_type";

    public static final int POPULAR = 0;
    public static final int TOP_RATED = 1;
    public static final int FAVORITES = 2;


    public static int getFilterChoice(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(FavoriteContract.CONTENT_AUTHORITY,
                        Context.MODE_PRIVATE);
        return prefs.getInt(Utility.FILTER_TYPE, Utility.POPULAR); // default to most popular

    }

}
