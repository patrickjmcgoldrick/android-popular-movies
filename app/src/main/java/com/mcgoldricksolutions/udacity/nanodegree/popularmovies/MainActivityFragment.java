package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by dirtbag on 6/20/16.
 */
public class MainActivityFragment extends Fragment {

    static String BASE_URL = "http://api.themoviedb.org/3";
    static String API_MOST_POPULAR = "/movie/popular";
    static String API_TOP_RATED = "/movie/top_rated";


    private MovieAdapter movieAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<MovieData>());

        FetchMovieDataTask fetchTask = new FetchMovieDataTask(movieAdapter);
        fetchTask.execute(BASE_URL + API_MOST_POPULAR + "?api_key=" + Key.API_KEY);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                Toast.makeText(getContext(), "position: " + position, Toast.LENGTH_SHORT).show();
                                                MovieData movieData = movieAdapter.getItem(position);
//                                                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
//                                                //Create intent
//                                                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//                                                intent.putExtra("title", item.getTitle());
//                                                intent.putExtra("image", item.getImage());
//
//                                                //Start details activity
//                                                startActivity(intent);
                                            }
                                        });
        gridView.setAdapter(movieAdapter);

//        flavorAdapter = new AndroidFlavorAdapter(getActivity(), Arrays.asList(androidFlavors));
//
//        // Get a reference to the ListView, and attach this adapter to it.
//        ListView listView = (ListView) rootView.findViewById(R.id.listview_flavor);
//        listView.setAdapter(flavorAdapter);

        return rootView;
    }
}
