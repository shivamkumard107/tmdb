package com.inshorts.tmdb.assign.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.inshorts.tmdb.assign.movie.db.dao.IBookmarkMovieDao;
import com.inshorts.tmdb.assign.movie.model.Category;
import com.inshorts.tmdb.assign.movie.model.Movie;
import com.inshorts.tmdb.assign.movie.model.responses.MovieListResponse;
import com.inshorts.tmdb.assign.movie.model.responses.MovieReviewResponse;
import com.inshorts.tmdb.assign.movie.model.responses.MovieTrailerResponse;
import com.inshorts.tmdb.assign.movie.network.MovieApiServices;
import com.inshorts.tmdb.assign.movie.util.threads.AppExecutors;


public final class MoviesRepository {

    private static MoviesRepository mMoviesRepository;
    private MovieApiServices mMovieApiServices;
    private IBookmarkMovieDao mMovieDao;
    private AppExecutors mThreadAppExecutors;
    private static final Object LOCK = new Object();
    private MutableLiveData<MovieListResponse> mMovieListResponse;
    private Movie mMovie;
    private final String TAG = MoviesRepository.class.getSimpleName();
    private MovieReviewResponse mMovieReviewResponse;
    private MovieTrailerResponse mMMovieTrailerResponse;
    private String language;


    private MoviesRepository(MovieApiServices movieApiServices, IBookmarkMovieDao movieDao, AppExecutors threadAppExecutors, String language) {
        mMovieApiServices = movieApiServices;
        mMovieDao = movieDao;
        mThreadAppExecutors = threadAppExecutors;
        this.language = language;
        mMovieListResponse = new MutableLiveData<>();
    }

    public synchronized static MoviesRepository getInstance(IBookmarkMovieDao movieDao, MovieApiServices movieApiServices, AppExecutors threadAppExecutors, String language) {
        if (mMoviesRepository == null) {
            synchronized (LOCK) {
                mMoviesRepository = new MoviesRepository(movieApiServices, movieDao, threadAppExecutors, language);
            }
        }
        return mMoviesRepository;
    }

    public LiveData<List<Movie>> getBookmarkMovies() {
        return mMovieDao.loadAll();
    }

    public LiveData<Movie> getSpecificBookmarkMovie(int id) {
        return mMovieDao.loadById(id);
    }

    public void bookmarkMovieOps(final Movie movie) {
        mThreadAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mMovieDao.checkForFavs(movie.getMovieId(), movie.getMovieTitle()) != null) {
                    mMovieDao.removeFromBookmarks(movie);
                    Log.d(TAG, "Movie Deleted");
                } else {
                    mMovieDao.addToBookmarks(movie);
                    Log.d(TAG, "Movie Saved");
                }
            }
        });
    }



    public LiveData<MovieListResponse> getMovies(Category category, final int page) {
        switch (category) {
            case TRENDING:
                loadTrendingMovies(page);
                break;
            case NOW_PLAYING:
                loadNowPlayingMovies(page);
                break;
        }
        return mMovieListResponse != null ? mMovieListResponse : new MutableLiveData<MovieListResponse>();
    }

    public LiveData<MovieListResponse> getSearchedMovies(Category category, final int page, String query) {
        loadSearchedMovies(category, page, query);
        return mMovieListResponse != null ? mMovieListResponse : new MutableLiveData<MovieListResponse>();
    }

    private void loadTrendingMovies(int page) {

        mMovieApiServices
                .getTrendingMovies(BuildConfig.API_KEY).enqueue(new Callback<MovieListResponse>() {
                    @Override
                    public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                        if (response.code() == 200) {
                            mMovieListResponse.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieListResponse> call, Throwable throwable) {
                        Log.e(TAG, "onFailure: " + "Failed!!!" + throwable.getMessage());
                    }
                });
        Log.d(TAG, "Trending Movies Loaded");
    }

    private void loadNowPlayingMovies(int page) {
        mMovieApiServices
                .getNowPlayingMovies(BuildConfig.API_KEY, language, page).enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                if (response.code() == 200) {
                    mMovieListResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + "Failed!!!" + throwable.getMessage());
            }
        });
        Log.d(TAG, "Trending Movies Loaded");
    }


    private void loadSearchedMovies(Category category, int page, String query) {
        mMovieApiServices
                .getSearchedMovies(BuildConfig.API_KEY, language, page, query).enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                if (response.code() == 200)
                    mMovieListResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + "Failed!!!" + throwable.getMessage());
            }
        });
        Log.d(TAG, "Trending Movies Loaded");
    }

    public Movie getMovieDetails(final int movieId) {
        try {
            mMovie = mMovieApiServices
                    .getMovie(movieId, BuildConfig.API_KEY, language)
                    .execute()
                    .body();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return mMovie != null ? mMovie : getDefaultMovie();
    }

    private Movie getDefaultMovie() {
        mMovie = new Movie();
        return mMovie;
    }

    public MovieReviewResponse getMovieReviews(final int movieId) {
        try {
            mMovieReviewResponse = mMovieApiServices
                    .getMovieReviews(movieId, BuildConfig.API_KEY, language)
                    .execute()
                    .body();
            Log.e(TAG, "Reviews Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return mMovieReviewResponse != null ? mMovieReviewResponse : new MovieReviewResponse();
    }

    public MovieTrailerResponse getMovieTrailers(final int movieId) {
        try {
            mMMovieTrailerResponse = mMovieApiServices
                    .getMovieTrailers(movieId, BuildConfig.API_KEY, language)
                    .execute()
                    .body();
            Log.e(TAG, "Trailers Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return mMMovieTrailerResponse != null ? mMMovieTrailerResponse : new MovieTrailerResponse();
    }

}
