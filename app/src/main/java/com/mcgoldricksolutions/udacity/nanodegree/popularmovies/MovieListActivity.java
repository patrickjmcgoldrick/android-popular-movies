package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data.FavoriteContract;

import java.util.ArrayList;

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

    //private MovieCursorAdapter movieAdapter;
    private MovieAdapter movieAdapter;


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private static final int FAVORTE_LOADER_ID = 0;

    /**
     * The filter choice is stored here until the "OK" button
     * of the dialog confirms a change and then the preferenc
     * will be updated.
     */
    private int temp_filter_choice = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //Cursor movieCursor = getContentResolver().query()
        //movieAdapter = new MovieCursorAdapter(this, movieCursor, 0);
        movieAdapter = new MovieAdapter(this, new ArrayList<MovieData>());

        //getLoaderManager().initLoader(FAVORTE_LOADER_ID, null, this);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) findViewById(R.id.movies_grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
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

        SharedPreferences prefs =
                MovieListActivity.this.getSharedPreferences(FavoriteContract.CONTENT_AUTHORITY,
                        Context.MODE_PRIVATE);
        int initialFilterStatus = prefs.getInt(Utility.FILTER_TYPE, 0); // default to most popular

        // Set the dialog title
        builder.setTitle("Select Filter:")
            // setup choice with list of filters
           .setSingleChoiceItems(R.array.filter_array, initialFilterStatus, new DialogInterface.OnClickListener() {

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

                    movieAdapter.switchMovieData();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });

        return builder.create();
    }

    //////////////////////////
    // Loader Callbacks
    //////////////////////////

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //movieAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        //movieAdapter.swapCursor(null);
    }
}
