package com.nikhil.popularmovies.io;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.nikhil.popularmovies.R;
import com.nikhil.popularmovies.data.MovieContract;
import com.nikhil.popularmovies.utils.Constants;
import com.nikhil.popularmovies.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by nikhil on 01/02/16.
 */
public class ParseMovieListJsonAsyncTask extends AsyncTask {

    private final String imageUrlSuffix;
    private SharedPreferences preferences;
    private Activity activity;
    private StringBuilder stringBuilder;
    private String postURL;
    private String backdropURL;

    public ParseMovieListJsonAsyncTask(Activity activity) {
        this.activity = activity;
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        imageUrlSuffix = Utility.getImageUrlSuffix(activity);


    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {


            JSONObject jsonObject = (JSONObject) params[0];
            JSONArray jsonArray = jsonObject.optJSONArray("results");


            Vector<ContentValues> cVVector = new Vector<ContentValues>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {


                JSONObject movieInfo = jsonArray.optJSONObject(i);
                stringBuilder = new StringBuilder(Constants.BASE_IMAGE_URL_PATH);
                stringBuilder.append(imageUrlSuffix);
                int prefixLenght = stringBuilder.toString().length();
                postURL = stringBuilder.append(movieInfo.optString(MovieContract.Movies.POSTER_PATH)).toString();
                stringBuilder.replace(prefixLenght, postURL.length(), "");
                backdropURL = stringBuilder.append(movieInfo.optString(MovieContract.Movies.BACKDROP_PATH)).toString();


                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieContract.Movies.PAGE, jsonObject.optString(MovieContract.Movies.PAGE));
                movieValues.put(MovieContract.Movies.POSTER_PATH, postURL);
                movieValues.put(MovieContract.Movies.ADULT, movieInfo.optString(MovieContract.Movies.ADULT));
                movieValues.put(MovieContract.Movies.OVERVIEW, movieInfo.optString(MovieContract.Movies.OVERVIEW));
                movieValues.put(MovieContract.Movies.RELEASE_DATE, movieInfo.optString(MovieContract.Movies.RELEASE_DATE));
                movieValues.put(MovieContract.Movies.MOVIE_ID, movieInfo.optString(MovieContract.Movies.MOVIE_ID));
                movieValues.put(MovieContract.Movies.ORIGINAL_TITLE, movieInfo.optString(MovieContract.Movies.ORIGINAL_TITLE));
                movieValues.put(MovieContract.Movies.ORIGINAL_LANGUAGE, movieInfo.optString(MovieContract.Movies.ORIGINAL_LANGUAGE));
                movieValues.put(MovieContract.Movies.TITLE, movieInfo.optString(MovieContract.Movies.TITLE));
                movieValues.put(MovieContract.Movies.BACKDROP_PATH, backdropURL);
                movieValues.put(MovieContract.Movies.POPULARITY, movieInfo.optString(MovieContract.Movies.POPULARITY));
                movieValues.put(MovieContract.Movies.VOTE_COUNT, movieInfo.optString(MovieContract.Movies.VOTE_COUNT));
                movieValues.put(MovieContract.Movies.VOTE_AVERAGE, movieInfo.optString(MovieContract.Movies.VOTE_AVERAGE));
                movieValues.put(MovieContract.Movies.SORT_BY, preferences.getString(activity.getString(R.string.pref_key), activity.getString(R.string.most_popular)));
                cVVector.add(movieValues);

            }

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                int inserted = activity.getContentResolver().bulkInsert(MovieContract.Movies.CONTENT_URI, cvArray);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
