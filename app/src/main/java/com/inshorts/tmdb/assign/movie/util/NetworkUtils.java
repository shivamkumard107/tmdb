package com.inshorts.tmdb.assign.movie.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public final class NetworkUtils {
    /**
     * Checks Network State returns true if connected
     *
     * @return Network State
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return false;
        }
    }
}
