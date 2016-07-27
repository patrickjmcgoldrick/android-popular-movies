package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parse JSON trailer info from API:
 *
 *      https://api.themoviedb.org/3/movie/{id}/trailers
 *
 * of the form:
 *
 * {
 * "id": 271110,
 * "quicktime": [ ],
 * "youtube": [
 *  {
 *   "name": "Official Trailer 1",
 *   "size": "HD",
 *   "source": "62DwFKBJNv8",
 *   "type": "Trailer"
 *  },
 *  {
 *   "name": "Official Trailer 2",
 *   "size": "HD",
 *   "source": "dKrVegVI0Us",
 *   "type": "Trailer"
 *  }
 * ]
 * }
 */
public class JsonTrailerAdapter extends BaseAdapter
                                implements AdapterView.OnItemClickListener {

    private static final String LOG_NAME = JsonTrailerAdapter.class.getSimpleName();

    // View Types
    private static final int TYPE_SEPARATOR =  0;
    private static final int TYPE_TRAILER   =  1;
    private static final int TYPE_REVIEW    =  2;

    // Trailer Labels
    private final static String YOUTUBE = "youtube"; // parent element
    private final static String NAME = "name";
    private final static String SIZE = "size";
    private final static String SOURCE = "source";
    private final static String TYPE = "type";


    // Review labels
    private final static String RESULTS = "results";  // parent element
    private final static String AUTHOR = "author";
    private final static String CONTENT = "content";
    private final static String URL = "url";

    private final static String SEPARATOR_TRAILERS = "Trailers:";
    private final static String SEPARATOR_REVIEWS = "Reviews:";

    // Member variables

    //String mJsonString;

    JSONArray mTrailersArray;

    JSONArray mReviewsArray;

    Context mContext;
    LayoutInflater mInflater;

    /**
     * Constructor
     * @param context
     */
    public JsonTrailerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);

    }

    /**
     * Set Trailer Json Data on adapter and update ui
     *
     * @param jsonString
     */
    public void setTrailerJsonData(String jsonString) {

        try {
            JSONObject object = new JSONObject(jsonString);

            mTrailersArray = object.getJSONArray(YOUTUBE);

            Log.d(LOG_NAME, "trailers + separator: " + getTrailerCount());

            notifyDataSetChanged();

        } catch (JSONException jsone) {
            Log.e(LOG_NAME, jsone.getMessage());
            if(jsonString != null) {
                Log.e(LOG_NAME, jsonString);
            }
        }

    }

    /**
     * Set Review Json Data on adapter and update ui
     *
     * @param jsonString
     */
    public void setReviewJsonData(String jsonString) {

        try {
            JSONObject object = new JSONObject(jsonString);

            mReviewsArray = object.getJSONArray(RESULTS);

            Log.d(LOG_NAME, "reviews + separator: " + getReviewCount());

            notifyDataSetChanged();

        } catch (JSONException jsone) {
            Log.e(LOG_NAME, jsone.getMessage());
            if(jsonString != null) {
                Log.e(LOG_NAME, jsonString);
            }
        }

    }

    private int getTrailerCount() {
        if(mTrailersArray != null) {
            int count = mTrailersArray.length();
            if(count > 0) {  // make sure we don't generate separator if no trailers
                return count + 1;
            } else {
                return count;
            }
        } else {
            return 0;
        }
    }

    private int getReviewCount() {
        if(mReviewsArray != null) {
            int count = mReviewsArray.length();
            if(count > 0) {  // make sure we don't generate separator if no reviews
                return count + 1;
            } else {
                return count;
            }
        } else {
            return 0;
        }
    }


    private boolean isTrailerSection(int position) {
        if(position < getTrailerCount()) {
            return true;
        }
        return false;
    }

    // calculate index where the reviews start
    private int getReviewIndex(int position) {
        return position - getTrailerCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isTrailerSection(position)) {
            if(position == 0) {
                return TYPE_SEPARATOR;
            } else {
                return TYPE_TRAILER;
            }
        } else {
            int reviewIndex = getReviewIndex(position);
            if(reviewIndex == 0) {
                return TYPE_SEPARATOR;
            } else {
                return TYPE_REVIEW;
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return getTrailerCount() + getReviewCount();
    }

    @Override
    public Object getItem(int position) {

        try {
            if(isTrailerSection(position)) {
                return mTrailersArray.getJSONObject(position-1);
            }

            int reviewIndex = getReviewIndex(position);
            if(reviewIndex < getReviewCount()) {
                return mReviewsArray.getJSONObject(reviewIndex - 1);
            }
        } catch (JSONException jsone) {
            Log.e(LOG_NAME, "failed on getItem - position: " + position);
            return null;
        }

        return null; // we should never get here
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MyViewHolder mViewHolder;

        int rowType = getItemViewType(position);

        TextView textView;
        JSONObject row;

        switch(rowType) {
            case TYPE_SEPARATOR:  // Separator Row

                view = mInflater.inflate(R.layout.list_item_header, parent, false);
                textView = (TextView) view.findViewById(R.id.section_separator);
                if(getReviewIndex(position) == 0) {
                    textView.setText(SEPARATOR_REVIEWS);
                } else {
                    textView.setText(SEPARATOR_TRAILERS);
                }
                break;

            case TYPE_TRAILER:    // Trailer Row

                if (view == null) {
                    view = mInflater.inflate(R.layout.list_item_trailer, parent, false);
                    mViewHolder = new MyViewHolder(view);
                    view.setTag(mViewHolder);
                } else {
                    mViewHolder = (MyViewHolder) view.getTag();
                }

                row = (JSONObject) getItem(position);

                try {
                    mViewHolder.trailerTitle.setText(row.getString(NAME));

                } catch (JSONException json) {
                    Log.e(LOG_NAME, "failed to access Trailer 'name'.");
                }
                break;


            case TYPE_REVIEW:   // Review Row

                view = mInflater.inflate(R.layout.list_item_review, parent, false);
                row = (JSONObject) getItem(position);
                try {
                    textView = (TextView) view.findViewById(R.id.reviewContent);
                    textView.setText(row.getString(CONTENT) + "\n\t -- " + row.getString(AUTHOR));

                } catch (JSONException json) {
                    Log.e(LOG_NAME, "failed to access Trailer 'name'.");
                } catch (NullPointerException npe) {
                    Log.e(LOG_NAME, "position: " + position);
                    throw npe;
                }
                break;
        }

        return view;
    }

    private class MyViewHolder {
        TextView trailerTitle;

        public MyViewHolder(View item) {
            trailerTitle = (TextView) item.findViewById(R.id.trailerTitle);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


        JSONObject row = (JSONObject)getItem(position);

        String movie_id = null;
        try {
            movie_id = row.getString(SOURCE);

        } catch (JSONException json) {
            Log.e(LOG_NAME, "failed to access Trailer 'source'.");
            return;
        }

        // stack overflow
        // http://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movie_id));
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + movie_id));
            mContext.startActivity(intent);
        }

    }


}
