package com.nikhil.popularmovies.io;


public interface AppRequest {
    public <T> void onRequestStarted(BaseTask<T> listener,String url);

    public <T> void onRequestCompleted(BaseTask<T> listener, String url);

    public <T> void onRequestFailed(BaseTask<T> listener,String url);
}



