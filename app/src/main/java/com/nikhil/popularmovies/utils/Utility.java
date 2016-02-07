package com.nikhil.popularmovies.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by nikhil on 21/12/15.
 */
public class Utility {


    public static String getImageUrlSuffix(Activity activity) {


        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        String tag;

        float density = activity.getResources().getDisplayMetrics().density;
        float screenWidthFloat = screenWidth / density;
        if (screenWidthFloat <= 92) {
            tag = "w92/";
        } else if (screenWidthFloat > 92 && screenWidthFloat <= 154) {
            tag = "w154/";
        } else if (screenWidthFloat > 154 && screenWidthFloat <= 185) {
            tag = "w185/";
        } else if (screenWidthFloat > 185 && screenWidthFloat <= 342) {
            tag = "w342/";
        } else if (screenWidthFloat > 342 && screenWidthFloat <= 500) {
            tag = "w500/";
        } else {
            tag = "w780/";
        }
        return tag;
    }


    public static boolean isNetworkConnectionAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED);
    }


    public static Typeface getFont(Context act) {
        Typeface customFont = Typeface.createFromAsset(act.getAssets(), "icomoon.ttf");
        return customFont;
    }
}
