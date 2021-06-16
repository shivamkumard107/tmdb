package com.inshorts.tmdb.assign.movie.ui.detail.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.inshorts.tmdb.assign.movie.MoviesRepository;
import com.inshorts.tmdb.assign.movie.model.Movie;
import com.inshorts.tmdb.assign.movie.model.responses.MovieReviewResponse;
import com.inshorts.tmdb.assign.movie.model.responses.MovieTrailerResponse;


public final class DetailViewModel extends ViewModel {
    private MoviesRepository mMoviesRepository;

    DetailViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    public Movie getMovieDetails(int movieId) {
        return mMoviesRepository
                .getMovieDetails(movieId);
    }

    public MovieTrailerResponse getMovieTrailersResponse(int movieId) {
        return mMoviesRepository
                .getMovieTrailers(movieId);
    }

    public MovieReviewResponse getMovieReviewsResponse(int movieId) {
        return mMoviesRepository
                .getMovieReviews(movieId);
    }

    public void bookmarkMovie(Movie movie) {
        mMoviesRepository.bookmarkMovieOps(movie);
    }

    public LiveData<Movie> getFav(int movieId) {
        return mMoviesRepository.getSpecificBookmarkMovie(movieId);
    }
}
