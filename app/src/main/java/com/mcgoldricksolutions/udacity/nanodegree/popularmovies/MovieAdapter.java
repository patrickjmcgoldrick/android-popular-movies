package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data.FavoriteContract;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dirtbag on 6/18/16.
 *
 * Adapter that holds movie data for grid view.
 */
public class MovieAdapter  extends ArrayAdapter<MovieData> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    /**
     * Save previously looked up data in a HashMap.
     */
    //HashMap<String, List<MovieData>> movieDataLookup = new HashMap<>();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context The current context. Used to inflate the layout file.
     * @param movies  A List of AndroidFlavor objects to display in a list
     */
    public MovieAdapter(Activity context, List<MovieData> movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movies);

        switchMovieData();
    }

    /**
     *  Test for previously looked up data.  If found, user appropriate saved data.
     *  Otherwise, Load data from url and save for later reuse.
     */
    public void switchMovieData() {

        SharedPreferences prefs = getContext().getSharedPreferences(FavoriteContract.CONTENT_AUTHORITY, Context.MODE_PRIVATE);
        int filter_type = prefs.getInt(Utility.FILTER_TYPE, 0);

        String filterName = null;

        switch (filter_type) {
            case Utility.POPULAR:
                filterName = Utility.API_MOST_POPULAR;
                break;

            case Utility.TOP_RATED:
                filterName = Utility.API_TOP_RATED;
                break;

            case Utility.FAVORITES:



                break;
            default:
                // error handling
        }


        FetchMovieDataTask fetchTask = new FetchMovieDataTask(this);
        fetchTask.execute(filterName);


    }

    /**
     * Allow the UI to call into adapter to switch out the relevant data.
     *
     * @param suffix
     * @param movieData
     */
    public void updateMovieData(String suffix, List<MovieData> movieData) {
//        // if new data, add to HashMap
//        if(suffix != null) {
//            movieDataLookup.put(suffix, movieData);
//        }

        // clear previous data
        clear();

        // update adapter with chnanged data
        addAll(movieData);

        // refresh UI
        notifyDataSetChanged();

    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        MovieData movie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView posterView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load(Utility.BASE_IMAGE_URL + movie.imageUrl).into(posterView);


        return convertView;
    }

}
