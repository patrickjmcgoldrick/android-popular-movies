package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.database.AbstractCursor;

import org.json.JSONArray;

import java.util.List;

/**
 * Takes a JSON Object and parses out appopriate data.
 *
 * Created by dirtbag on 8/20/16.
 */
public class MovieListCursor extends AbstractCursor {

    List<Movie> mMovies;

    static String[] COLUMN_NAMES = {"id", "imageUrl"};
    static String INVALID_COLUMN = "INVALID_COLUMN";

    /**
     * Creates a Cursor object we can use in our list or grid
     * without bothering with a DB because it is temporary data.
     *
     * @param movies
     */
    public MovieListCursor(List<Movie> movies) {
        mMovies = movies;
    }

    ///////////////////
    // Implemntentation of Abstract methods.
    ///////////////////

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public String[] getColumnNames() {
        return COLUMN_NAMES;
    }

    @Override
    public String getString(int col) {

        Movie movie = mMovies.get(mPos);
        switch(col) {
            case 0:
                return movie.id + "";
            case 1:
                return movie.imageUrl;
        }
        return INVALID_COLUMN;
    }

    @Override
    public short getShort(int col) {
        return 0;
    }

    @Override
    public int getInt(int col) {

        Movie movie = mMovies.get(mPos);
        switch(col) {
            case 0:
                return movie.id;
            case 1:
                return 0;
        }
        return -1;
    }

    @Override
    public long getLong(int col) {
        return getInt(col);
    }

    @Override
    public float getFloat(int col) {
        return 0;
    }

    @Override
    public double getDouble(int col) {
        return 0;
    }

    @Override
    public boolean isNull(int col) {
        return false;
    }

}
