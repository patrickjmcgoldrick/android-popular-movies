package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {

    static String BASE_URL = "http://api.themoviedb.org/3";
    static String API_MOST_POPULAR = "/movie/popular";
    static String API_TOP_RATED = "/movie/top_rated";


    private MovieAdapter movieAdapter;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        movieAdapter = new MovieAdapter(this, new ArrayList<MovieData>());

        FetchMovieDataTask fetchTask = new FetchMovieDataTask(movieAdapter);
        fetchTask.execute(BASE_URL + API_MOST_POPULAR + "?api_key=" + Key.API_KEY);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) findViewById(R.id.movies_grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getApplication(), "position: " + position, Toast.LENGTH_SHORT).show();
                MovieData movieData = movieAdapter.getItem(position);
                //Create intent
                Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
                intent.putExtra("movie_id", "" + movieData.id);
                intent.putExtra("movie_detail", movieData);
                //Start details activity
                startActivity(intent);
            }
        });
        gridView.setAdapter(movieAdapter);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }


}
