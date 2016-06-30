package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by dirtbag on 6/19/16.
 *
 * Background process to handle loanding JSON from https://www.themoviedb.org/documentation/api
 *
 * Then load the movie data JSON into the movie adapter for the grid view.
 */
public class FetchMovieDataTask extends AsyncTask<String, String, List<MovieData>> {

    OkHttpClient client = new OkHttpClient();
    MovieAdapter movieAdapter;

    private Exception exceptionToBeThrown;

    String urlSuffix;

    public FetchMovieDataTask(MovieAdapter movieAdapter) {
        this.movieAdapter = movieAdapter;
    }


    /**
     * main heavy lifter of this class.  Does all work here in the background process
     *
     * @param suffix
     * @return
     */
    protected List<MovieData> doInBackground(String... suffix) {

        int count = suffix.length;

        if(count > 1) {
            System.out.println("Error: too many urls");
            return null;
        }
        this.urlSuffix = suffix[0];

        String url = Utility.BASE_URL + this.urlSuffix + "?api_key=" + Key.API_KEY;

        System.out.println(url);

        String response;
        try {
            response = downloadJsonFromUrl(url);
        } catch (IOException ioe) {
            exceptionToBeThrown = ioe;
            return null;
        }

        MovieJsonParser parser = new MovieJsonParser();
        List<MovieData> movies = null;

        try {
            movies = parser.parse(response);
        } catch(JSONException jsone) {
            exceptionToBeThrown = jsone;
            return null;
        }

        return movies;
    }

    /**
     * This is executed on the UI thread and called after the heavly lifter is done.
     *
     * @param movies
     */
    protected void onPostExecute(List<MovieData> movies) {

        if(exceptionToBeThrown != null) {
            Toast.makeText(movieAdapter.getContext(),
                    "Exception: " + exceptionToBeThrown.toString(),
                    Toast.LENGTH_LONG)
                    .show();
        } else {
            movieAdapter.updateMovieData(urlSuffix, movies);
        }
    }

    /**
     * take in url and call that url and return the body.
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String downloadJsonFromUrl(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();


    }
}
