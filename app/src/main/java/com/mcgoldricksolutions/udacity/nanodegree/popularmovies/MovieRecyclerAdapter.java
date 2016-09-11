package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data.FavoriteContract;
import com.squareup.picasso.Picasso;

/**
 * Created by dirtbag on 8/15/16.
 */
public class MovieRecyclerAdapter
        extends RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder> {

    public static String LOG_TAG = MovieRecyclerAdapter.class.getSimpleName();

    private Cursor mMovies = null;

    private AppCompatActivity mAppCompatActivity;
    private boolean mTwoPane;

    public MovieRecyclerAdapter(AppCompatActivity appCompatActivity, boolean twoPane) {
        mAppCompatActivity = appCompatActivity;
        mTwoPane = twoPane;
    }

    public Context getContext() {
        return mAppCompatActivity;
    }

    public void swapCursor(Cursor movies) {
        if (movies != null) {
            mMovies = movies;
            notifyDataSetChanged();
        }
    }

    public Cursor getCursor() {
        return mMovies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(mMovies == null) {
            return;
        }

        mMovies.moveToPosition(position);

        // find column index from db or from ArrayList object
        int posterIndex = -1;
        if(Utility.getFilterChoice(mAppCompatActivity) == Utility.FAVORITES) {
            posterIndex = mMovies.getColumnIndexOrThrow(FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL);
        } else {
            posterIndex = mMovies.getColumnIndexOrThrow(MovieListCursor.COLUMN_NAMES[1]);
        }

        // load poster image into UI
        if(posterIndex != -1) {
            Picasso.with(mAppCompatActivity)
                    .load(Utility.BASE_IMAGE_URL + mMovies.getString(posterIndex))
                    .into(holder.mPosterView);
        }

        holder.setPosition(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Movie movie = null;


                // grab movie data for chosen film
                int filterChoice = Utility.getFilterChoice(mAppCompatActivity);
                if(filterChoice == Utility.POPULAR
                        || filterChoice == Utility.TOP_RATED) {


                    MovieListCursor listCursor = (MovieListCursor) getCursor();
                    listCursor.moveToPosition(position);
                    movie = listCursor.getCurrentMovie();

                } else {
                    Cursor cursor = getCursor();
                    cursor.moveToPosition(position);
                    int movieId = cursor.getInt(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID));

                    movie = new Movie(movieId,
                            cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL)),
                            cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE)),
                            cursor.getDouble(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_USER_RATING))
                    );

                }

                Log.d(LOG_TAG,"Movie: " + movie);
                Log.d(LOG_TAG,"filterChoice: " + filterChoice);

                // pass appropriate data to detail screen
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(MovieDetailFragment.ARG_MOVIE_ID, movie.id);
                    arguments.putParcelable(MovieDetailFragment.ARG_MOVIE_DETAIL, movie);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    mAppCompatActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailFragment.ARG_MOVIE_ID, movie.id);
                    intent.putExtra(MovieDetailFragment.ARG_MOVIE_DETAIL, movie);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mMovies != null) {
            return mMovies.getCount();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPosterView;
        public int mPosition;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPosterView = (ImageView) view.findViewById(R.id.movie_image);
        }

        public void setPosition(int position) {
            mPosition = position;
        }
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mPosterView.gets + "'";
//        }
    }
}
