package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data.FavoriteContract;
import com.squareup.picasso.Picasso;

/**
 * Created by dirtbag on 8/15/16.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieRecyclerAdapterViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    //final private ForecastAdapterOnClickHandler mClickHandler;
    //final private View mEmptyView;
    //final private ItemChoiceManager mICM;


    public MovieRecyclerAdapter(Context context, int choiceMode) {
        mContext = context;
        //mClickHandler = dh;
        //mEmptyView = emptyView;
        //mICM = new ItemChoiceManager(this);
        //mICM.setChoiceMode(choiceMode);
    }

    public Context getContext() {
        return mContext;
    }
    /*
        This takes advantage of the fact that the viewGroup passed to onCreateViewHolder is the
        RecyclerView that will be used to contain the view, so that it can get the current
        ItemSelectionManager from the view.

        One could implement this pattern without modifying RecyclerView by taking advantage
        of the view tag to store the ItemChoiceManager.
     */

    @Override
    public MovieRecyclerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if ( viewGroup instanceof RecyclerView ) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list, viewGroup, false);
            view.setFocusable(true);
            return new MovieRecyclerAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(MovieRecyclerAdapterViewHolder movieRecyclerAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        int posterIndex = mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL);
        if(posterIndex != -1) {
            Picasso.with(mContext)
                    .load(Utility.BASE_IMAGE_URL + mCursor.getString(posterIndex))
                    .into(movieRecyclerAdapterViewHolder.mPosterView);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        //mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public class MovieRecyclerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mPosterView;

        public MovieRecyclerAdapterViewHolder(View view) {
            super(view);
            mPosterView = (ImageView) view.findViewById(R.id.movie_image);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
//            int dateColumnIndex = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
//            mClickHandler.onClick(mCursor.getLong(dateColumnIndex), this);
//            mICM.onClick(this);

        }
    }
}
