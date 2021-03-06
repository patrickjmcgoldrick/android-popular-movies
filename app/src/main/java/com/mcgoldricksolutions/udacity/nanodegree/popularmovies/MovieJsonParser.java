package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dirtbag on 6/19/16.
 *
 * Takes care of parsing movie list data and converting each movie item into
 * MovieData object.
 */
public class MovieJsonParser {


    /**
     * Parses the movies details from json string
     *
     * @param jsonString
     * @return
     */
    public List<Movie> parse(String jsonString) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject object = new JSONObject(jsonString);
        JSONArray results = object.getJSONArray("results");

        int id;
        String title;
        String posterUrl;
        String description;
        String releaseDate;
        double userRating;

        for (int i=0; i< results.length(); i++) {
            JSONObject movie = results.getJSONObject(i);

            id = movie.getInt("id");
            title = movie.getString("original_title");
            posterUrl = movie.getString("poster_path");
            description = movie.getString("overview");
            releaseDate = movie.getString("release_date");
            userRating = movie.getDouble("vote_average");

            Movie movieData = new Movie(id, title, posterUrl, description, releaseDate, userRating);
            movies.add(movieData);
        }

        return movies;
    }
}
