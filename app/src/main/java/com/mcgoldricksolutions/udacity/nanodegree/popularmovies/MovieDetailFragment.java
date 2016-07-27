package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data.FavoriteContract;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends ListFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_MOVIE_ID = "movie_id";

    public static final String ARG_MOVIE_DETAIL = "movie_detail";

    private MovieData mMovie;
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

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView listView = getListView();

        // Show the dummy content as text in a TextView.
        if (mMovie != null) {
            View headerView = getLayoutInflater(savedInstanceState).inflate(R.layout.movie_detail, listView, false);

            ((TextView) headerView.findViewById(R.id.movie_title)).setText(mMovie.title);

            ImageView posterThumbnail = (ImageView) headerView.findViewById(R.id.movie_thumbnail);
            Picasso.with(getContext()).load(Utility.BASE_IMAGE_URL + mMovie.imageUrl).into(posterThumbnail);


            ((TextView) headerView.findViewById(R.id.movie_description)).setText(mMovie.description);
            ((TextView) headerView.findViewById(R.id.movie_release_date)).setText(mMovie.getReleaseDateYear());
            //((TextView) rootView.findViewById(R.id.movie_description)).setText(mMovie.description);
            ((TextView) headerView.findViewById(R.id.movie_user_rating)).setText(mMovie.userRating + "/10");

            Button btnFavorite = (Button) headerView.findViewById(R.id.btn_favorite);
            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),
                            "id: " + MovieDetailFragment.this.mMovie.id,
                            Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues();
                    values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, MovieDetailFragment.this.mMovie.id);
                    values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, MovieDetailFragment.this.mMovie.title);
                    values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL, MovieDetailFragment.this.mMovie.imageUrl);
                    values.put(FavoriteContract.FavoriteEntry.COLUMN_DESCRIPTION, MovieDetailFragment.this.mMovie.description);
                    values.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, MovieDetailFragment.this.mMovie.releaseDate);
                    values.put(FavoriteContract.FavoriteEntry.COLUMN_USER_RATING, MovieDetailFragment.this.mMovie.userRating);
                    ContentResolver resolver = getContext().getContentResolver();
                    Uri uri = resolver.insert(FavoriteContract.FavoriteEntry.CONTENT_URI, values);
                    Log.d("MovieDetailFragment", "return URI: " + uri);
                }
            });

            listView.addHeaderView(headerView);


            // add trailers
            JsonTrailerAdapter trailerAdapter = new JsonTrailerAdapter(this.getContext());
            setListAdapter(trailerAdapter);
            getListView().setOnItemClickListener(trailerAdapter);


            // Fetch Trailers
            FetchTrailersTask fetchTrailers = new FetchTrailersTask(trailerAdapter);

            String trailersUrl = Utility.BASE_URL
                    + Utility.MOVIE + mMovie.id
                    + Utility.TRAILERS
                    + Utility.API_PARAMETER + Key.API_KEY;
            fetchTrailers.execute(trailersUrl);

            // Fetch Reviews
            FetchReviewsTask fetchReviews = new FetchReviewsTask(trailerAdapter);

            String reviewsUrl = Utility.BASE_URL
                    + Utility.MOVIE + mMovie.id
                    + Utility.REVIEWS
                    + Utility.API_PARAMETER + Key.API_KEY;
            fetchReviews.execute(reviewsUrl);

        }

    }
}
