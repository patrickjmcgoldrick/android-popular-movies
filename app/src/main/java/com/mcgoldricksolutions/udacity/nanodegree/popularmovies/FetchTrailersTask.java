package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

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
public class FetchTrailersTask extends AsyncTask<String, String, String> {

    private static final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

    OkHttpClient client = new OkHttpClient();

    JsonTrailerAdapter mTrailerAdapter;

    private Exception exceptionToBeThrown;

    String urlSuffix;

    public FetchTrailersTask(JsonTrailerAdapter trailerAdapter) {
        this.mTrailerAdapter = trailerAdapter;
    }


    /**
     * main heavy lifter of this class.  Does all work here in the background process
     *
     * @param suffix
     * @return
     */
    protected String doInBackground(String... suffix) {

        int count = suffix.length;

        if(count > 1) {
            System.out.println("Error: too many urls");
            return null;
        }
        this.urlSuffix = suffix[0];

        System.out.println(this.urlSuffix);

        String response;
        try {
            response = downloadJsonFromUrl(this.urlSuffix);
        } catch (IOException ioe) {
            exceptionToBeThrown = ioe;
            return null;
        }

        return response;
    }

    /**
     * This is executed on the UI thread and called after the heavly lifting is done.
     *
     * @param jsonString
     */
    protected void onPostExecute(String jsonString) {

        if(exceptionToBeThrown != null) {
            Log.e(LOG_TAG,
                    "Exception: " + exceptionToBeThrown.toString());
        } else {
            mTrailerAdapter.setJsonData(jsonString);
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
