package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data.FavoriteContract;
import com.squareup.picasso.Picasso;

/**
 * Created by dirtbag on 7/1/16.
 */
public class MovieCursorAdapter extends CursorAdapter {

    public MovieCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView posterView = (ImageView) view.findViewById(R.id.movie_image);
        int posterIndex = cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL);
        if(posterIndex != -1) {
            Picasso.with(context).load(Utility.BASE_IMAGE_URL + cursor.getString(posterIndex)).into(posterView);
        }
    }
}
