package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
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
public class FetchMovieDataTask extends AsyncTask<String, String, List<Movie>> {

    public static String LOG_TAG = FetchMovieDataTask.class.getSimpleName();

    OkHttpClient client = new OkHttpClient();

    MovieRecyclerAdapter mMovieRecyclerAdapter;

    private Exception exceptionToBeThrown;

    String urlSuffix;

    public FetchMovieDataTask(MovieRecyclerAdapter movieRecyclerAdapter) {
        mMovieRecyclerAdapter = movieRecyclerAdapter;
    }


    /**
     * main heavy lifter of this class.  Does all work here in the background process
     *
     * @param suffix
     * @return
     */
    protected List<Movie> doInBackground(String... suffix) {

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
        List<Movie> movies = null;

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
    protected void onPostExecute(List<Movie> movies) {

        if(exceptionToBeThrown != null) {
            Toast.makeText(mMovieRecyclerAdapter.getContext(),
                    "Exception: " + exceptionToBeThrown.toString(),
                    Toast.LENGTH_LONG)
                    .show();
        } else {
            Cursor movieCursor = new MovieListCursor(movies);
            mMovieRecyclerAdapter.swapCursor(movieCursor);
            Log.i(LOG_TAG, "cursorSize: " + movieCursor.getCount());
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
