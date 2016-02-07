package com.nikhil.popularmovies.io;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;

import org.json.JSONObject;

import java.util.Map;

public abstract class BaseTask<T> extends Request<T> {
    protected static final String PROTOCOL_CHARSET = "utf-8";
    /**
     * Content type for request.
     */

    static final int MY_SOCKET_TIMEOUT_MS = 30000;
    static final int DEFAULT_MAX_RETRIES = 1;
    static final int DEFAULT_BACKOFF_MULT = 0;

    JSONObject jsonResponse;

    public BaseTask(int method, String url, ErrorListener listener) {
        super(method, url, listener);
        setShouldCache(true);

        this.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DEFAULT_MAX_RETRIES,
                DEFAULT_BACKOFF_MULT));
    }



    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }

    public JSONObject getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(JSONObject jsonResponse) {
        this.jsonResponse = jsonResponse;
    }


}
