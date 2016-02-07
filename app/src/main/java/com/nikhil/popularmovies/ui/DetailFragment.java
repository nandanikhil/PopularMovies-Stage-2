package com.nikhil.popularmovies.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nikhil.popularmovies.R;
import com.nikhil.popularmovies.data.MovieContract;
import com.nikhil.popularmovies.io.ApiRequests;
import com.nikhil.popularmovies.io.AppRequest;
import com.nikhil.popularmovies.io.BaseTask;
import com.nikhil.popularmovies.io.ParseMovieDetailJsonAsyncTask;
import com.nikhil.popularmovies.utils.Constants;
import com.nikhil.popularmovies.utils.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by nikhil on 22/12/15.
 */
public class DetailFragment extends Fragment implements AppRequest, LoaderManager.LoaderCallbacks<Cursor> {


    private String movie_id;
    private View view;
    private ImageView moviePoster;
    private String imageUrlSuffix;
    private LinearLayout progress;
    private TextView movie_title;
    private TextView movieDuration;
    private TextView movieYear;
    private TextView movieRating;
    private TextView movieOverview;


    private static final int DETAIL_LOADER = 0;
    private static final int TRAILER_LOADER = 1;
    private static final int REVIEW_LOADER = 2;
    private static final int FAVOURITE_LOADER = 3;


    private static final String[] MOVIE_COLUMNS = {

            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies._ID,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.PAGE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.POSTER_PATH,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ADULT,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.OVERVIEW,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.RELEASE_DATE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.MOVIE_ID,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ORIGINAL_TITLE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ORIGINAL_LANGUAGE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.TITLE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.BACKDROP_PATH,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.POPULARITY,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.VOTE_COUNT,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.VOTE_AVERAGE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.FAVOURED,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.SHOWED,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.DOWNLOADED,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.SORT_BY

    };
    private static final String[] TRAILER_COLUMNS = {

            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers._ID,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.NAME,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.SIZE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.SOURCE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.TYPE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.MOVIE_ID
    };
    private static final String[] REVIEW_COLUMNS = {

            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews._ID,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.PAGE,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.TOTAL_PAGE,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.TOTAL_RESULTS,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.ID_REVIEWS,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.AUTHOR,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.CONTENT,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.URL,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.MOVIE_ID

    };

    private static final String[] FAVOURITE_MOVIE_COLUMNS = {

            MovieContract.Favourites.TABLE_NAME + "." + MovieContract.Favourites._ID,
            MovieContract.Favourites.PAGE,
            MovieContract.Favourites.POSTER_PATH,
            MovieContract.Favourites.ADULT,
            MovieContract.Favourites.OVERVIEW,
            MovieContract.Favourites.RELEASE_DATE,
            MovieContract.Favourites.MOVIE_ID,
            MovieContract.Favourites.ORIGINAL_TITLE,
            MovieContract.Favourites.ORIGINAL_LANGUAGE,
            MovieContract.Favourites.TITLE,
            MovieContract.Favourites.BACKDROP_PATH,
            MovieContract.Favourites.POPULARITY,
            MovieContract.Favourites.VOTE_COUNT,
            MovieContract.Favourites.VOTE_AVERAGE,
            MovieContract.Favourites.FAVOURED,
            MovieContract.Favourites.SHOWED,
            MovieContract.Favourites.DOWNLOADED,
            MovieContract.Favourites.SORT_BY
    };


    public static int COL_ID = 0;
    public static int COL_PAGE = 1;
    public static int COL_POSTER_PATH = 2;
    public static int COL_ADULT = 3;
    public static int COL_OVERVIEW = 4;
    public static int COL_RELEASE_DATE = 5;
    public static int COL_MOVIE_ID = 6;
    public static int COL_ORIGINAL_TITLE = 7;
    public static int COL_ORIGINAL_LANG = 8;
    public static int COL_TITLE = 9;
    public static int COL_BACKDROP_PATH = 10;
    public static int COL_POPULARITY = 11;
    public static int COL_VOTE_COUNT = 12;
    public static int COL_VOTE_AVERAGE = 13;
    public static int COL_FAVOURED = 14;
    public static int COL_SHOWED = 15;
    public static int COL_DOWNLOADED = 16;
    public static int COL_SORT_BY = 17;


    public static int COL_TRAILER_ID = 0;
    public static int COL_TRAILER_NAME = 1;
    public static int COL_TRAILER_SIZE = 2;
    public static int COL_TRAILER_SOURCE = 3;
    public static int COL_TRAILER_TYPE = 4;
    public static int COL_TRAILER_MOVIE_ID = 5;


    public static int COL_REVIEW_AUTHOR = 5;
    public static int COL_REVIEW_CONTENT = 6;
    public static int COL_REVIEW_URL = 7;

    private SharedPreferences preferences;
    private TextView popularityTextView;
    private ImageView backdropImg;
    private String backdropURL;
    private FloatingActionButton favouriteIcon;
    private static ContentValues movieValues;
    private boolean favoured = false;
    private String playTrailer;
    private FloatingActionButton playIcon;
    private ListView reviewList;
    private ReviewAdapter reviewListAdapter;
    private FloatingActionButton reviewIcon;
    private FloatingActionButton shareIcon;
    private String movieTitle;
    private RelativeLayout mainView;
    private int noOfReviews;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            movie_id = bundle.getString("id");
        }
        imageUrlSuffix = Utility.getImageUrlSuffix(getActivity());
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        playTrailer = "https://www.youtube.com/watch?v=";

        movieValues = new ContentValues();

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.detail_fragmnet, container, false);
        progress = (LinearLayout) view.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
        backdropImg = (ImageView) view.findViewById(R.id.backdropImg);
        moviePoster.setVisibility(View.GONE);
        movie_title = (TextView) view.findViewById(R.id.movie_title);
        movieYear = (TextView) view.findViewById(R.id.movieYear);
        movieDuration = (TextView) view.findViewById(R.id.movieDuration);
        movieRating = (TextView) view.findViewById(R.id.movieRating);
        popularityTextView = (TextView) view.findViewById(R.id.popularity);
        movieOverview = (TextView) view.findViewById(R.id.movieOverview);
        favouriteIcon = (FloatingActionButton) view.findViewById(R.id.favouriteIcon);
        playIcon = (FloatingActionButton) view.findViewById(R.id.playIcon);
        reviewIcon = (FloatingActionButton) view.findViewById(R.id.reviewIcon);
        shareIcon = (FloatingActionButton) view.findViewById(R.id.shareIcon);
        mainView = (RelativeLayout) view.findViewById(R.id.mainView);
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,
                        movieTitle + "Watch : " + playTrailer);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getString(R.string.share_links)));
            }
        });
        reviewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (noOfReviews > 0) {

                    if (reviewList.getVisibility() == View.GONE) {
                        reviewList.setVisibility(View.VISIBLE);
                        mainView.setVisibility(View.GONE);
                    } else {
                        mainView.setVisibility(View.VISIBLE);
                        reviewList.setVisibility(View.GONE);
                    }
                } else
                {
                    Toast.makeText(getActivity(), "NO Reviews Present", Toast.LENGTH_SHORT).show();
                }
            }


        });
        reviewList = (ListView) view.findViewById(R.id.reviewList);
        reviewListAdapter = new ReviewAdapter(getActivity(), null, 0);
        reviewList.setAdapter(reviewListAdapter);
        reviewList.setVisibility(View.GONE);

        playIcon.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(playTrailer));
                                            startActivity(intent);
                                        }
                                    }

        );

        favouriteIcon.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View view) {
                                                 if (favoured) {
                                                     getActivity().getContentResolver().delete(MovieContract.Favourites.buildMoviesUriWithMovieId(movie_id), null, null);
                                                     Toast.makeText(getContext(), "REMOVED FROM FAVOURITES!", Toast.LENGTH_SHORT).show();
                                                     favoured = false;
                                                     favouriteIcon.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.star_big_off));
                                                 } else {
                                                     getActivity().getContentResolver().insert(MovieContract.Favourites.buildMovieUri(), movieValues);
                                                     Toast.makeText(getContext(), "ADDED TO FAVOURITES!", Toast.LENGTH_SHORT).show();
                                                     favouriteIcon.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.star_big_on));
                                                     favoured = true;
                                                 }
                                             }
                                         }

        );

        updateView();

        return view;
    }


    private void updateView() {
        StringBuilder stringBuilder = new StringBuilder(Constants.BASE_URL_DETAIL);
        stringBuilder.append(String.valueOf(movie_id));
        stringBuilder.append(Constants.API_TAG);
        stringBuilder.append(Constants.MOVIE_DB_API_KEY);
        stringBuilder.append(Constants.FETCH_TRAILER_TAG);
        stringBuilder.append("trailers,reviews");
        if (Utility.isNetworkConnectionAvailable(getActivity())) {
            ApiRequests.getInstance().fetchMovies(getActivity(), this, stringBuilder.toString());
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener, String url) {
        progress.setVisibility(View.VISIBLE);

    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener, String url) {


        if (listener.getJsonResponse() != null && listener.getJsonResponse() instanceof JSONObject) {

            new ParseMovieDetailJsonAsyncTask(getActivity()).execute(listener.getJsonResponse());


            JSONObject movieArrayReviews = listener.getJsonResponse().optJSONObject("reviews");
            noOfReviews = movieArrayReviews.optInt("total_results");
        }
        progress.setVisibility(View.GONE);
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener, String url) {
        progress.setVisibility(View.GONE);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (movie_id != null) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            switch (id) {
                case DETAIL_LOADER:
                    String sorting = preferences.getString(getString(R.string.pref_key), getString(R.string.most_popular));
                    if (sorting.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {
                        return new CursorLoader(getActivity(),
                                MovieContract.Favourites.buildMovieUri(),
                                FAVOURITE_MOVIE_COLUMNS,
                                MovieContract.Favourites.MOVIE_ID + " = ?",
                                new String[]{movie_id},
                                null);
                    }
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Movies.buildMoviesUriWithMovieId(movie_id),
                            MOVIE_COLUMNS,
                            null,
                            null,
                            null
                    );
                case FAVOURITE_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Favourites.buildMovieUri(),
                            FAVOURITE_MOVIE_COLUMNS,
                            null,
                            null,
                            null
                    );
                case TRAILER_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Trailers.buildMoviesUriWithMovieId(movie_id),
                            TRAILER_COLUMNS,
                            null,
                            null,
                            null
                    );
                case REVIEW_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Reviews.buildMoviesUriWithMovieId(movie_id),
                            REVIEW_COLUMNS,
                            null,
                            null,
                            null
                    );
            }
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (!data.moveToFirst()) {
            return;
        }
        switch (loader.getId()) {
            case DETAIL_LOADER:
                movieTitle = data.getString(COL_ORIGINAL_TITLE);
                movie_title.setText(data.getString(COL_ORIGINAL_TITLE));
                movieOverview.setText(data.getString(COL_OVERVIEW));
                movieYear.setText("Release Date: " + data.getString(COL_RELEASE_DATE));

                Picasso
                        .with(getActivity())
                        .load(data.getString(COL_POSTER_PATH))
                        .transform(new RoundedCornersTransformation(10, 10))
                        .into(moviePoster);

                moviePoster.setVisibility(View.VISIBLE);
                movieDuration.setText("vote count" + data.getString(COL_VOTE_COUNT));
                String movieId = data.getString(COL_MOVIE_ID);
                String popularity = data.getString(COL_POPULARITY);
                double pop = Double.parseDouble(popularity);
                popularity = String.valueOf((double) Math.round(pop * 10d) / 10d);
                popularityTextView.setText("Popularity : " + popularity);

                String votAvg = data.getString(COL_VOTE_AVERAGE);
                double vote = Double.parseDouble(votAvg);
                votAvg = String.valueOf((double) Math.round(vote * 10d) / 10d);
                movieRating.setText(votAvg);
                backdropURL = data.getString(COL_BACKDROP_PATH);

                Picasso.with(getActivity()).load(data.getString(COL_BACKDROP_PATH)).networkPolicy(NetworkPolicy.OFFLINE).into(backdropImg, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getActivity()).load(backdropURL).error(R.mipmap.ic_launcher).into(backdropImg);
                    }
                });

                if (movieValues.size() == 0) {
                    movieValues.put(MovieContract.Movies.PAGE, data.getString(COL_PAGE));
                    movieValues.put(MovieContract.Movies.POSTER_PATH, data.getString(COL_POSTER_PATH));
                    movieValues.put(MovieContract.Movies.ADULT, data.getString(COL_ADULT));
                    movieValues.put(MovieContract.Movies.OVERVIEW, data.getString(COL_OVERVIEW));
                    movieValues.put(MovieContract.Movies.RELEASE_DATE, data.getString(COL_RELEASE_DATE));
                    movieValues.put(MovieContract.Movies.MOVIE_ID, movie_id);
                    movieValues.put(MovieContract.Movies.ORIGINAL_TITLE, data.getString(COL_ORIGINAL_TITLE));
                    movieValues.put(MovieContract.Movies.ORIGINAL_LANGUAGE, data.getString(COL_ORIGINAL_LANG));
                    movieValues.put(MovieContract.Movies.TITLE, data.getString(COL_TITLE));
                    movieValues.put(MovieContract.Movies.BACKDROP_PATH, backdropURL);
                    movieValues.put(MovieContract.Movies.POPULARITY, popularity);
                    movieValues.put(MovieContract.Movies.VOTE_COUNT, data.getString(COL_VOTE_COUNT));
                    movieValues.put(MovieContract.Movies.VOTE_AVERAGE, votAvg);
                    movieValues.put(MovieContract.Movies.FAVOURED, "1");
                    movieValues.put(MovieContract.Movies.SHOWED, data.getString(COL_SHOWED));
                    movieValues.put(MovieContract.Movies.DOWNLOADED, data.getString(COL_DOWNLOADED));
                    movieValues.put(MovieContract.Movies.SORT_BY, data.getString(COL_SORT_BY));
                }
                break;
            case FAVOURITE_LOADER:
                FloatingActionButton fab;
                if (data.moveToFirst()) {
                    do {
                        if (data.getString(COL_MOVIE_ID).equalsIgnoreCase(movie_id)) {
                            favoured = true;
                        }
                    }
                    while (data.moveToNext());
                }
                if (favoured) {
                    favouriteIcon.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.star_big_on));
                } else {
                    favouriteIcon.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.star_big_off));

                }

                break;
            case TRAILER_LOADER:
                int iter = 0;
                if (data.moveToFirst()) {
                    do {
                        iter++;
                        if (iter == 1) {
                            playTrailer += data.getString(DetailFragment.COL_TRAILER_SOURCE);
                        }
                    }
                    while (data.moveToNext());
                }
                break;
            case REVIEW_LOADER:
                if (data.moveToFirst()) {
                    do {
                        reviewListAdapter.swapCursor(data);
                    }
                    while (data.moveToNext());
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown Loader");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        reviewListAdapter.swapCursor(null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(TRAILER_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        getLoaderManager().initLoader(FAVOURITE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
}
