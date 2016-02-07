package com.nikhil.popularmovies.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nikhil.popularmovies.R;

/**
 * Created by nikhil on 04/02/16.
 */
public class ReviewAdapter extends CursorAdapter {
    public ReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {

        public final TextView author;
        public final TextView contentView;
        public final TextView urlView;

        public ViewHolder(View view) {
            author = (TextView) view.findViewById(R.id.review_author);
            contentView = (TextView) view.findViewById(R.id.review_content);
            urlView = (TextView) view.findViewById(R.id.review_url);

        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /*
       This is where we fill-in the views with the contents of the cursor.
    */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String author_name = cursor.getString(DetailFragment.COL_REVIEW_AUTHOR);
        final String content = cursor.getString(DetailFragment.COL_REVIEW_CONTENT);
        final String url = cursor.getString(DetailFragment.COL_REVIEW_URL);

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.author.setText("Author:  " + author_name);
        viewHolder.contentView.setText("Content:  " + content);
        viewHolder.urlView.setText("Look more at:  " + url);
    }
}

