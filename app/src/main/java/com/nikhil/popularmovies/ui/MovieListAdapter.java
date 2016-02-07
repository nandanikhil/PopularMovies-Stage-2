package com.nikhil.popularmovies.ui;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikhil.popularmovies.R;
import com.nikhil.popularmovies.utils.CursorRecyclerViewAdapter;
import com.nikhil.popularmovies.utils.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by nikhil on 21/12/15.
 */
public class MovieListAdapter extends CursorRecyclerViewAdapter<MovieListAdapter.ListItemViewHolder> {

    private DetailFragment detailFragment = null;
    private Bundle bundle = null;
    public MainActivity mainActivity;
    private String imageUrlSuffix = null;
    private boolean isTwoPane;
    private int container;

    private Cursor globalCursor = null;
    private String url;


    public MovieListAdapter(Cursor cursor, boolean isTwoPane, MainActivity activity) {
        super(activity, cursor);
        mainActivity = activity;
        this.isTwoPane = isTwoPane;
        imageUrlSuffix = Utility.getImageUrlSuffix(activity);
        container = (isTwoPane ? R.id.detailContainer : R.id.mainContainer);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final Cursor cursor) {
        globalCursor = cursor;

        if (globalCursor != null) {

            detailFragment = new DetailFragment();
            if (cursor != null && cursor.getPosition() == 0) {
                cursor.move(0);
                bundle = new Bundle();
                bundle.putString("id", cursor.getString(MoviesListFragment.COL_MOVIE_ID));
                detailFragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction().add(container, detailFragment, DetailFragment.class.getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
            }


            final String imageUrl = globalCursor.getString(MoviesListFragment.COL_POSTER_PATH);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    detailFragment = new DetailFragment();
                    bundle.putString("id", (String) v.getTag());
                    detailFragment.setArguments(bundle);
                    mainActivity.getSupportFragmentManager().beginTransaction().add(container, detailFragment, DetailFragment.class.getSimpleName()).addToBackStack(null).commitAllowingStateLoss();

                }
            });


            Picasso.with(mainActivity).load(imageUrl)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(holder.moviePoster, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(mainActivity).load(imageUrl)
                                    .transform(new RoundedCornersTransformation(10, 10)).into(holder.moviePoster);

                        }
                    });

            String rating = globalCursor.getString(MoviesListFragment.COL_VOTE_AVERAGE);
            holder.itemView.setTag(globalCursor.getString(MoviesListFragment.COL_MOVIE_ID));
            holder.rating.setText(rating);

            String popularity = globalCursor.getString(MoviesListFragment.COL_POPULARITY);
            int pos = popularity.indexOf(".");
            holder.popularity.setText(popularity.substring(0, pos >= 0 ? pos : 0));
        }

    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mainActivity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
        ListItemViewHolder listItemViewHolder = new ListItemViewHolder(view, mainActivity);
        return listItemViewHolder;

    }


    public static class ListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView ratingIcon;
        public TextView popularityIcon;
        public TextView popularity;
        public TextView rating;
        public ImageView moviePoster;

        public ListItemViewHolder(View itemView, Activity activity) {
            super(itemView);
            setIsRecyclable(false);
            moviePoster = (ImageView) itemView.findViewById(R.id.moviePoster);
            rating = (TextView) itemView.findViewById(R.id.rating);
            ratingIcon = (TextView) itemView.findViewById(R.id.ratingIcon);
            ratingIcon.setTypeface(Utility.getFont(activity));
            popularity = (TextView) itemView.findViewById(R.id.popularity);
            popularityIcon = (TextView) itemView.findViewById(R.id.popularityIcon);
            popularityIcon.setTypeface(Utility.getFont(activity));


        }

    }
}
