package com.nikhil.popularmovies.io;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nikhil.popularmovies.R;
import com.nikhil.popularmovies.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiRequests {
    private RequestQueue mRequestQueue;
    private static ApiRequests apiRequests = null;
    public static final String PREFERENCES_FILE = "CatalogPreference";
    private String url;


    public static ApiRequests getInstance() {
        if (apiRequests == null) {
            apiRequests = new ApiRequests();
            return apiRequests;
        }
        return apiRequests;
    }


    public void fetchMovies(Context context, AppRequest appRequest, String url) {
        try {

            if(Utility.isNetworkConnectionAvailable(context)) {
                this.url = url;
                mRequestQueue = RequestManager.getnstance(context);
                VolleyErrorListener error = new VolleyErrorListener();
                final HttpRequests requests = new HttpRequests(Request.Method.GET, error, appRequest, url);
                error.setRequestLister(appRequest, requests);
                if (mRequestQueue != null) {
                    mRequestQueue.cancelAll(url);
                }
                requests.setTag(url);
                mRequestQueue.add(requests);
                appRequest.onRequestStarted(requests, url);
            }else {
                Toast.makeText(context, R.string.no_internet,Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Will be reponsible for listening errors
     * <p/>
     * *
     */
    class VolleyErrorListener implements Response.ErrorListener {
        private AppRequest listener;
        private BaseTask<?> task;
        private String api_name;

        void setRequestLister(AppRequest listener, BaseTask<?> task) {
            this.listener = listener;
            this.task = task;
        }

        @Override
        public void onErrorResponse(VolleyError error) {

            String json = null;
            NetworkResponse response = error.networkResponse;
            if (response != null && response.data != null) {
                json = new String(response.data);
                json = trimMessage(json, "message");
//                if (json != null) Utility.displayMessage(response.statusCode, json +" on "+api_name==null?"":api_name);

            } else {
//                Utility.displayMessage(500, "Unexpected server error on " +api_name==null?"":api_name);
            }
            if (listener != null)
                listener.onRequestFailed(task, url);
        }


        public String trimMessage(String json, String key) {
            String trimmedString = null;

            try {
                JSONObject obj = new JSONObject(json);
                trimmedString = obj.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            return trimmedString;
        }


    }
}