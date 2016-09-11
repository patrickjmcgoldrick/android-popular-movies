package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data.FavoriteContract;

import java.util.ArrayList;

import okhttp3.internal.Util;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {

    public static String LOG_TAG = MovieListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private MovieRecyclerAdapter movieRecyclerAdapter;

    private static final int FAVORTE_LOADER_ID = 0;

    private int temp_filter_choice = -1;

    /**
     * The filter choice is stored here until the "OK" button
     * of the dialog confirms a change and then the preferenc
     * will be updated.
     */
//    private int temp_filter_choice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // test for two pane view
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_list);
        assert recyclerView != null;

        movieRecyclerAdapter = new MovieRecyclerAdapter(this, mTwoPane);
        recyclerView.setAdapter(movieRecyclerAdapter);

        loadFilterData();

    }

    /**
     * Lookup chosen filter and start a background thread
     * to load appropriate data into recycler adapter
     */
    private void loadFilterData() {
        int filter = Utility.getFilterChoice(this);

        FetchMovieDataTask dataTask = null;
        switch (filter) {
            case Utility.POPULAR:
                // start by loading popular movies
                dataTask = new FetchMovieDataTask(movieRecyclerAdapter);
                dataTask.execute(Utility.API_MOST_POPULAR);
                break;
            case Utility.TOP_RATED:
                // start by loading top rated movies
                dataTask = new FetchMovieDataTask(movieRecyclerAdapter);
                dataTask.execute(Utility.API_TOP_RATED);
                break;
            case Utility.FAVORITES:
                // load favorite data from database
                getSupportLoaderManager().initLoader(FAVORTE_LOADER_ID, null, this);
                break;
        }

        // TODO set header to show what we are filtering on.
    }

    /**
     * Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            // popup dialog
            createDialogSingleChoice().show();

            return true;
        }

        Toast.makeText(this, "Item: " + item.toString(), Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Create Dialog for movie filter
     *
     * Content from: https://abhik1987.wordpress.com/2013/02/06/android-alert-dialog-with-single-choice-item-selection-implementation/
     * @return
     */
    public Dialog createDialogSingleChoice() {

        //Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the dialog title
        builder.setTitle("Select Filter:")
            // setup choice with list of filters
           .setSingleChoiceItems(R.array.filter_array, Utility.getFilterChoice(this), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int filter_choice_id) {
                    // TODO Auto-generated method stub
                    temp_filter_choice = filter_choice_id;
                }
            })

            // set dialog icon
            .setIcon(android.R.drawable.ic_dialog_alert)

            // Set the action buttons
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK, so save the result somewhere
                    // or return them to the component that opened the dialog
                    SharedPreferences prefs =
                            MovieListActivity.this.getSharedPreferences(FavoriteContract.CONTENT_AUTHORITY,
                                                                        Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(Utility.FILTER_TYPE, temp_filter_choice);
                    editor.apply();

                    loadFilterData();

                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });

        return builder.create();
    }

    private void fetchMovieData(String filterName) {
        FetchMovieDataTask fetchTask = new FetchMovieDataTask(movieRecyclerAdapter);
        fetchTask.execute(filterName);
    }

    //////////////////////////
    // Loader Callbacks
    //////////////////////////

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        return new CursorLoader(this,
                FavoriteContract.FavoriteEntry.CONTENT_URI,
                null,
                null, null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        movieRecyclerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        movieRecyclerAdapter.swapCursor(null);
    }
}
