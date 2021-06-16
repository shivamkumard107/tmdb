package com.inshorts.tmdb.assign.movie.ui.main.callbacks;

import java.util.List;

import com.inshorts.tmdb.assign.movie.model.Category;
import com.inshorts.tmdb.assign.movie.model.MovieNetworkLite;

public interface MovieListCallBack {

    void inProgress();

    void onFinished(List<MovieNetworkLite> movies, Category category);
}
