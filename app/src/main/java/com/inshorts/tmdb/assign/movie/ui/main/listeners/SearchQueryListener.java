package com.inshorts.tmdb.assign.movie.ui.main.listeners;

import android.os.Handler;
import android.util.Log;
import android.widget.SearchView;

import com.inshorts.tmdb.assign.movie.model.Category;
import com.inshorts.tmdb.assign.movie.ui.main.callbacks.BookmarkMoviesCallback;
import com.inshorts.tmdb.assign.movie.ui.main.viewmodel.MainViewModel;

public class SearchQueryListener implements SearchView.OnQueryTextListener {
    private MainViewModel mMainViewModel;
    private BookmarkMoviesCallback mBookmarkMovieCallback;
    private static final String TAG = "SearchQueryListener";
    private static final Handler mHandler = new android.os.Handler();

    public SearchQueryListener(MainViewModel mMainViewModel, BookmarkMoviesCallback mBookmarkMovieCallback) {
        this.mMainViewModel = mMainViewModel;
        this.mBookmarkMovieCallback = mBookmarkMovieCallback;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (s != null) {
            Log.e(TAG, "onQueryTextSubmit: ");
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String s) {
        if (s != null && s.length() != 0) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchQuery(s);
                }
            }, 300);
        }
        return true;
    }

    private void searchQuery(String query) {
        Log.e(TAG, "searchQuery: " + query);
        mBookmarkMovieCallback.loadSearchedQuery(Category.SEARCH, 1, query);

    }
}
