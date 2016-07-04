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
    // Constants
    private final static String YOUTUBE = "youtube";

    private final static String NAME = "name";

    private final static String SIZE = "size";

    private final static String SOURCE = "source";

    private final static String TYPE = "type";

    // Member variables

    String mJsonString;
    JSONArray mYoutubeArray;

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
     * Set Json Data on adapter and update ui
     *
     * @param jsonString
     */
    public void setJsonData(String jsonString) {
        this.mJsonString = jsonString;

        try {
            JSONObject object = new JSONObject(mJsonString);

            mYoutubeArray = object.getJSONArray(YOUTUBE);

            Log.d(LOG_NAME, "array length: " + mYoutubeArray.length());


            notifyDataSetChanged();

        } catch (JSONException jsone) {
            Log.e(LOG_NAME, jsone.getMessage());
            if(jsonString != null) {
                Log.e(LOG_NAME, mJsonString);
            }
            mJsonString = null;
        }

    }

    @Override
    public int getCount() {
        if(mJsonString == null) {
            return 0;
        } else {
            return mYoutubeArray.length();
        }
    }

    @Override
    public Object getItem(int position) {

        if (mYoutubeArray == null) {
            return null;
        }

        Object retObject = null;
        try {
            retObject = mYoutubeArray.getJSONObject(position);
        } catch (JSONException jsone) {
            Log.e(LOG_NAME, "failed on getItem");
        }

        return retObject;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.trailer_list_item, parent, false);
            mViewHolder = new MyViewHolder(view);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) view.getTag();
        }

        JSONObject row = (JSONObject)getItem(position);

        try {
            mViewHolder.trailerTitle.setText(row.getString(NAME));

        } catch (JSONException json) {
            Log.e(LOG_NAME, "failed to access Trailer 'name'.");
        }
        return view;
    }

//    //public Context getContext() {
//        return mContext;
//    }

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
