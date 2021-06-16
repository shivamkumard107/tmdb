package com.inshorts.tmdb.assign.movie.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.inshorts.tmdb.assign.movie.MoviesRepository;
import com.inshorts.tmdb.assign.movie.model.Category;
import com.inshorts.tmdb.assign.movie.model.Movie;
import com.inshorts.tmdb.assign.movie.model.responses.MovieListResponse;


public final class MainViewModel extends ViewModel {
    private MoviesRepository mMoviesRepository;

    MainViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    public LiveData<MovieListResponse> getMovies(Category category, int page) {
        return mMoviesRepository.getMovies(category, page); //TODO understand this
    }

    public LiveData<MovieListResponse> getSearchedMovies(Category category, int page, String query) {
        return mMoviesRepository.getSearchedMovies(category, page, query);
    }

    public LiveData<List<Movie>> getBookmarkMovies() {
        return mMoviesRepository.getBookmarkMovies();
    }
}
