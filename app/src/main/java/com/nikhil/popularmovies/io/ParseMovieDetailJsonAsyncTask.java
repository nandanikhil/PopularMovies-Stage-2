package com.nikhil.popularmovies.io;

import android.app.Activity;
import android.content.ContentValues;
import android.os.AsyncTask;

import com.nikhil.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by nikhil on 03/02/16.
 */
public class ParseMovieDetailJsonAsyncTask extends AsyncTask {

    private Activity activity;

    public ParseMovieDetailJsonAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {


        final String RESULT1 = "trailers";
        final String RESULT2 = "reviews";
        final String RESULT3 = "genres";
        final String YOUTUBE = "youtube";

        JSONObject jsonObject = (JSONObject) params[0];

        JSONArray movieArrayTrailer = jsonObject.optJSONObject("trailers").optJSONArray("youtube");
        JSONObject movieArrayReviews = jsonObject.optJSONObject("reviews");


        String movie_id = jsonObject.optString(MovieContract.Movies.MOVIE_ID);

        Vector<ContentValues> cVVectorTrailer = new Vector<ContentValues>(movieArrayTrailer.length());

        for (int i = 0; i < movieArrayTrailer.length(); i++) {

            JSONObject trailerInfo = movieArrayTrailer.optJSONObject(i);

            ContentValues movieValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.

            movieValues.put(MovieContract.Trailers.NAME, trailerInfo.optString(MovieContract.Trailers.NAME));
            movieValues.put(MovieContract.Trailers.SIZE, trailerInfo.optString(MovieContract.Trailers.SIZE));
            movieValues.put(MovieContract.Trailers.SOURCE, trailerInfo.optString(MovieContract.Trailers.SOURCE));
            movieValues.put(MovieContract.Trailers.TYPE, trailerInfo.optString(MovieContract.Trailers.TYPE));
            movieValues.put(MovieContract.Trailers.MOVIE_ID, movie_id);
            cVVectorTrailer.add(movieValues);

        }
        int inserted = 0;
        // add to database
        if (cVVectorTrailer.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVectorTrailer.size()];
            cVVectorTrailer.toArray(cvArray);
            inserted = activity.getContentResolver().bulkInsert(MovieContract.Trailers.CONTENT_URI, cvArray);
        }


        JSONArray reviews = movieArrayReviews.optJSONArray("results");

        Vector<ContentValues> cVVectorReviews = new Vector<ContentValues>(reviews.length());

        for (int j = 0; j < reviews.length(); j++) {

            JSONObject reviewsInfo = reviews.optJSONObject(j);
            ContentValues movieValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.

            movieValues.put(MovieContract.Reviews.PAGE, movieArrayReviews.optString(MovieContract.Reviews.PAGE));
            movieValues.put(MovieContract.Reviews.TOTAL_PAGE, movieArrayReviews.optString(MovieContract.Reviews.TOTAL_PAGE));
            movieValues.put(MovieContract.Reviews.TOTAL_RESULTS, movieArrayReviews.optString(MovieContract.Reviews.TOTAL_RESULTS));
            movieValues.put(MovieContract.Reviews.ID_REVIEWS, reviewsInfo.optString(MovieContract.Reviews.ID_REVIEWS));
            movieValues.put(MovieContract.Reviews.AUTHOR, reviewsInfo.optString(MovieContract.Reviews.AUTHOR));
            movieValues.put(MovieContract.Reviews.CONTENT, reviewsInfo.optString(MovieContract.Reviews.CONTENT));
            movieValues.put(MovieContract.Reviews.URL, reviewsInfo.optString(MovieContract.Reviews.URL));
            movieValues.put(MovieContract.Reviews.MOVIE_ID, movie_id);

            cVVectorReviews.add(movieValues);

        }
        inserted = 0;
        // add to database
        if (cVVectorReviews.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVectorReviews.size()];
            cVVectorReviews.toArray(cvArray);
            inserted = activity.getContentResolver().bulkInsert(MovieContract.Reviews.CONTENT_URI, cvArray);
        }


        return null;
    }
}
