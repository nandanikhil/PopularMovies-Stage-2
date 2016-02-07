package com.nikhil.popularmovies.io;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Request Manager Class for Managing the Network Requests
 */
public class RequestManager {
    private static RequestManager mRequestManager;
    /**
     * Queue which Manages the Network Requests :-)
     */
    private static RequestQueue mRequestQueue;

    private RequestManager() {
    }

    public static RequestManager get(Context context) {
        if (mRequestManager == null)
            mRequestManager = new RequestManager();
        return mRequestManager;
    }

    /**
     * @param context application context
     */
    public static RequestQueue getnstance(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }


}
