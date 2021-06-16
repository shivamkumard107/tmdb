package com.inshorts.tmdb.assign.movie.ui.main.listeners;

import androidx.appcompat.widget.PopupMenu;
import android.view.MenuItem;

import com.inshorts.tmdb.assign.movie.R;
import com.inshorts.tmdb.assign.movie.model.Category;
import com.inshorts.tmdb.assign.movie.ui.main.callbacks.BookmarkMoviesCallback;
import com.inshorts.tmdb.assign.movie.ui.main.viewmodel.MainViewModel;

public class CategoryMenuListener implements PopupMenu.OnMenuItemClickListener {

    private BookmarkMoviesCallback mBookmarkMoviesCallbackContext;
    private MainViewModel mMainViewModel;

    public CategoryMenuListener(MainViewModel mainViewModel, BookmarkMoviesCallback bookmarkMoviesCallbackContext) {
        this.mMainViewModel = mainViewModel;
        this.mBookmarkMoviesCallbackContext = bookmarkMoviesCallbackContext;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_top_rated:
                mBookmarkMoviesCallbackContext.loadMovies(Category.TRENDING, 1);
//                new MovieListAsync(mMainViewModel, Category.TRENDING, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_now_playing:
                mBookmarkMoviesCallbackContext.loadMovies(Category.NOW_PLAYING, 1);
//                new MovieListAsync(mMainViewModel, Category.NOW_PLAYING, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_bookmark:
                mBookmarkMoviesCallbackContext.loadBookmarkMovies();
                return true;
        }
        return false;
    }
}
