package com.nikhil.popularmovies.io;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ripansharma on 26/05/15.
 */
public class HttpRequests extends BaseTask<JSONObject> {
    private Map<String, String> headers = new HashMap<String, String>();
    private Context mContext;
    private AppRequest appRequest;
    private Map<String, String> mParams = new HashMap<String, String>();
    private String url;


    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }


    public HttpRequests(int method, Response.ErrorListener listener,
                        AppRequest appRequest, String url) {

        super(method, url, listener);
        this.appRequest = appRequest;
        this.url = url;

    }


    @Override
    public Map<String, String> getParams() {

        return mParams;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }
        return volleyError;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        setJsonResponse(response);
        appRequest.onRequestCompleted(this, url);


    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        JSONObject json = null;
        try {
            json = new JSONObject(new String(response.data));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
    }

}
