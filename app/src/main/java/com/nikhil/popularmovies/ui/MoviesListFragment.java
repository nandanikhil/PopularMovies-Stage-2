package com.nikhil.popularmovies.ui;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nikhil.popularmovies.R;
import com.nikhil.popularmovies.data.MovieContract;
import com.nikhil.popularmovies.io.ApiRequests;
import com.nikhil.popularmovies.io.AppRequest;
import com.nikhil.popularmovies.io.BaseTask;
import com.nikhil.popularmovies.io.ParseMovieListJsonAsyncTask;
import com.nikhil.popularmovies.utils.Constants;
import com.nikhil.popularmovies.utils.Utility;

import org.json.JSONObject;

/**
 * Created by nikhil on 20/12/15.
 */
public class MoviesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AppRequest {

    private View view;
    private RecyclerView moviesList;
    private GridLayoutManager gridLayoutManager;
    private MovieListAdapter movieListAdapter = null;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    private MainActivity activity;
    private boolean mTwoPane;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noInternetConnection;
    private String sortType;
    private SharedPreferences preferences;


    private static final int MOVIE_LOADER = 0;
    private static final String[] MOVIE_COLUMNS = {

            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies._ID,
            MovieContract.Movies.PAGE,
            MovieContract.Movies.POSTER_PATH,
            MovieContract.Movies.ADULT,
            MovieContract.Movies.OVERVIEW,
            MovieContract.Movies.RELEASE_DATE,
            MovieContract.Movies.MOVIE_ID,
            MovieContract.Movies.ORIGINAL_TITLE,
            MovieContract.Movies.ORIGINAL_LANGUAGE,
            MovieContract.Movies.TITLE,
            MovieContract.Movies.BACKDROP_PATH,
            MovieContract.Movies.POPULARITY,
            MovieContract.Movies.VOTE_COUNT,
            MovieContract.Movies.VOTE_AVERAGE,
            MovieContract.Movies.FAVOURED
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
            MovieContract.Favourites.FAVOURED
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        activity = ((MainActivity) getActivity());
        if (getArguments() != null) {
            mTwoPane = getArguments().getBoolean("isTwoPane");
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        movieListAdapter = new MovieListAdapter(null, mTwoPane, activity);


    }


    public MoviesListFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
//        updateView();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_fragment, container, false);
        moviesList = (RecyclerView) view.findViewById(R.id.moviesList);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_swipe_refresh);
        noInternetConnection = (TextView) view.findViewById(R.id.noInternetConnection);
        noInternetConnection.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utility.isNetworkConnectionAvailable(getActivity())) {
                    updateView();
                } else {
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                }


            }
        });
        if (mTwoPane) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else {

            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        }

        moviesList.setAdapter(movieListAdapter);
        moviesList.setLayoutManager(gridLayoutManager);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }


        updateView();


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        if (!getLoaderManager().hasRunningLoaders())
            getLoaderManager().initLoader(MOVIE_LOADER, null, MoviesListFragment.this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener, String url) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener, String url) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        if (listener.getJsonResponse() != null && listener.getJsonResponse() instanceof JSONObject) {

            new ParseMovieListJsonAsyncTask(activity).execute(listener.getJsonResponse());

        }

    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener, String url) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        noInternetConnection.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), R.string.request_failed, Toast.LENGTH_SHORT).show();
    }


    public void updateView() {

        sortType = preferences.getString(getString(R.string.pref_key), getString(R.string.most_popular));

        if (sortType.equalsIgnoreCase(getString(R.string.most_popular))) {
            ApiRequests.getInstance().fetchMovies(getActivity(), MoviesListFragment.this, Constants.BASE_URL_POPULAR_MOVIES + Constants.MOVIE_DB_API_KEY);
        } else {
            ApiRequests.getInstance().fetchMovies(getActivity(), MoviesListFragment.this, Constants.BASE_URL_HIGHEST_RATED_MOVIES + Constants.MOVIE_DB_API_KEY);
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = MovieContract.Movies._ID + " ASC";
        Uri movie = MovieContract.Movies.buildMovieUri();
        Uri fav = MovieContract.Favourites.buildMovieUri();
        String sorting = preferences.getString(getString(R.string.pref_key), getString(R.string.most_popular));
        if (sorting.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {
            return new CursorLoader(getActivity(),
                    fav,
                    FAVOURITE_MOVIE_COLUMNS,
                    MovieContract.Favourites.FAVOURED + " = ?",
                    new String[]{"1"},
                    sortOrder);
        }
        return new CursorLoader(getActivity(),
                movie,
                MOVIE_COLUMNS,
                MovieContract.Movies.SORT_BY + " = ?",
                new String[]{sorting},
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (movieListAdapter != null)
            movieListAdapter.swapCursor(data);
        swipeRefreshLayout.setRefreshing(false);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            moviesList.smoothScrollToPosition(mPosition);
        }
        try {
//            TextView info=(TextView)rootView.findViewById(R.id.empty);
            if (movieListAdapter.getItemCount() == 0) {
                String sorting = PreferenceManager.getDefaultSharedPreferences(activity).getString(getString(R.string.pref_key), "");
                if (sorting.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {
                    noInternetConnection.setText("Favourite List is Empty!");
                }
                noInternetConnection.setVisibility(View.VISIBLE);
            } else {
                noInternetConnection.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        movieListAdapter.swapCursor(null);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to GridView.INVALID_POSITION,
        // so check for that before storing.

        mPosition = ((LinearLayoutManager) moviesList.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }
}
