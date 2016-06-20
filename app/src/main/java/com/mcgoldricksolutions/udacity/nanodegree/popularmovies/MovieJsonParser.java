package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dirtbag on 6/19/16.
 */
public class MovieJsonParser {


    /**
     * Parses the movies details from json string
     *
     * @param jsonString
     * @return
     */
    public List<MovieData> parse(String jsonString) {
        ArrayList<MovieData> movies = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray results = object.getJSONArray("results");

            int id;
            String title;
            String posterUrl;

            for (int i=0; i< results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);

                id = movie.getInt("id");
                title = movie.getString("original_title");
                posterUrl = movie.getString("poster_path");

                MovieData movieData = new MovieData(id, title, posterUrl);
                movies.add(movieData);
            }

        } catch (JSONException jsone) {
            return null;
        }
        return movies;
    }
}
