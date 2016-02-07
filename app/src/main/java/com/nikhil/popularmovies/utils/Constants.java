package com.nikhil.popularmovies.utils;

import com.nikhil.popularmovies.BuildConfig;

/**
 * Created by nikhil on 06/12/15.
 */
public class Constants {

    public static final String MOVIE_DB_API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    public static final String BASE_IMAGE_URL_PATH = "http://image.tmdb.org/t/p/";
    public static final String BASE_URL_POPULAR_MOVIES = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";
    public static final String BASE_URL_HIGHEST_RATED_MOVIES = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";
    public static final String BASE_URL_DETAIL = "https://api.themoviedb.org/3/movie/";

    public static final String API_TAG = "?api_key=";
    public static final String FETCH_TRAILER_TAG = "&append_to_response=";




}

