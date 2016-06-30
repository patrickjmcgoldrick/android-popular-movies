package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_MOVIE_ID = "movie_id";

    public static final String ARG_MOVIE_DETAIL = "movie_detail";

    MovieData mMovie;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE_ID)) {
            // Load the Movie content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mMovie = (MovieData) getArguments().getParcelable(ARG_MOVIE_DETAIL);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

         }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mMovie != null) {
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(mMovie.title);

            ImageView posterThumbnail = (ImageView) rootView.findViewById(R.id.movie_thumbnail);
            Picasso.with(getContext()).load(Utility.BASE_IMAGE_URL + mMovie.imageUrl).into(posterThumbnail);


            ((TextView) rootView.findViewById(R.id.movie_description)).setText(mMovie.description);
            ((TextView) rootView.findViewById(R.id.movie_release_date)).setText(mMovie.getReleaseDateYear());
            //((TextView) rootView.findViewById(R.id.movie_description)).setText(mMovie.description);
            ((TextView) rootView.findViewById(R.id.movie_user_rating)).setText(mMovie.userRating + "/10");

        }

        return rootView;
    }
}
