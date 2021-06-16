package com.inshorts.tmdb.assign.movie.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import com.inshorts.tmdb.assign.movie.model.Movie;
import com.inshorts.tmdb.assign.movie.model.responses.MovieListResponse;
import com.inshorts.tmdb.assign.movie.model.responses.MovieReviewResponse;
import com.inshorts.tmdb.assign.movie.model.responses.MovieTrailerResponse;

public interface MovieApiServices {

    /**
     * Api Interface that facilitates request calls to TMDB Api and returns response similaro to
     * model in cast
     *
     * @param apiKey   Api Key from TMDB
     * @param language Based on language preference
     * @param page     page to load ,min 1 max 1000
     * @return json response
     */

    @GET("search/movie")
    Call<MovieListResponse> getSearchedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page,
            @Query("query") String query
    );

    @GET("trending/movie/day")
    Call<MovieListResponse> getTrendingMovies(
            @Query("api_key") String apiKey
    );

    @GET("movie/now_playing")
    Call<MovieListResponse> getNowPlayingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewResponse> getMovieReviews(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Call<MovieTrailerResponse> getMovieTrailers(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

}
