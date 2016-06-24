package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by dirtbag on 6/19/16.
 */
public class FetchMovieDataTask extends AsyncTask<String, String, List<MovieData>> {

    OkHttpClient client = new OkHttpClient();
    MovieAdapter movieAdapter;

    public FetchMovieDataTask(MovieAdapter movieAdapter) {
        this.movieAdapter = movieAdapter;
    }


    protected List<MovieData> doInBackground(String... urls) {
        int count = urls.length;

        if(count > 1) {
            System.out.println("Error: too many urls");
            return null;
        }

        String response;
        try {
            response = downloadJsonFromUrl(urls[0]);
        } catch (IOException ioe) {
            System.out.println( "IO Exception: " + ioe.toString());
            return null;
        }

        System.out.println(response);

        MovieJsonParser parser = new MovieJsonParser();
        List<MovieData> movies = parser.parse(response);


        return movies;
    }

    protected void onProgressUpdate(String progress) {
       // setProgressPercent(progress);
    }

    protected void onPostExecute(List<MovieData> movies) {

        movieAdapter.updateMovieData(movies);

        movieAdapter.notifyDataSetChanged();

        //showDialog("Downloaded " + result + " bytes");
    }


    public String downloadJsonFromUrl(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();


    }
}
