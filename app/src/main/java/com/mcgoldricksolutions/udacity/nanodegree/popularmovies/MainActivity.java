package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static String BASE_URL = "http://api.themoviedb.org/3";
    static String API_MOST_POPULAR = "/movie/popular";
    static String API_TOP_RATED = "/movie/top_rated";



    private MovieAdapter movieAdapter;

    MovieData[] movies = {
            new MovieData(1, "Die Hard", "blah"),
            new MovieData(2, "Die Hard 2", "blah"),
            new MovieData(3, "Die Hard with a Vengeance", "blah"),
            new MovieData(4, "Live Free or Die Hard", "blah"),
            new MovieData(5, "A Good Day to Die Hard", "blah"),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        movieAdapter = new MovieAdapter(this, new ArrayList<MovieData>());

        FetchMovieDataTask fetchTask = new FetchMovieDataTask(movieAdapter);
        fetchTask.execute(BASE_URL + API_MOST_POPULAR + "?api_key=" + Key.API_KEY);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) findViewById(R.id.movies_grid);
        gridView.setAdapter(movieAdapter);


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
