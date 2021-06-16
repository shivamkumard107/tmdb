package com.inshorts.tmdb.assign.movie.ui.main.callbacks;

import com.inshorts.tmdb.assign.movie.model.Category;

public interface BookmarkMoviesCallback {

    void loadBookmarkMovies();
    void loadMovies(Category category, int page);
    void loadSearchedQuery(Category category, int page, String query);
}
