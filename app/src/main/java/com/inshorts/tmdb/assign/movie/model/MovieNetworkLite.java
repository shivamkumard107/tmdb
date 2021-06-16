package com.inshorts.tmdb.assign.movie.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Objects;


@Parcel(Parcel.Serialization.BEAN)
public final class MovieNetworkLite implements IMovie {

    @SerializedName("vote_average")
    private float voterAverage;

    @SerializedName("poster_path")
    private String moviePoster;

    @SerializedName("id")
    private int movieId;

    @SerializedName("title")
    private String movieTitle;

    MovieNetworkLite() {
    }

    public MovieNetworkLite(float voterAverage, String moviePoster, int movieId, String movieTitle) {
        this.voterAverage = voterAverage;
        this.moviePoster = moviePoster;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }

    @Override
    public float getVoterAverage() {
        return voterAverage;
    }

    @Override
    public String getMoviePoster() {
        return moviePoster;
    }

    @Override
    public int getMovieId() {
        return movieId;
    }

    @Override
    public String getMovieTitle() {
        return movieTitle;
    }

    @Override
    public String toString() {
        return "MovieNetworkLite{" +
                "voterAverage=" + voterAverage +
                ", moviePoster='" + moviePoster + '\'' +
                ", movieId=" + movieId +
                ", movieTitle='" + movieTitle + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object vo) {
        if (this == vo) return true;
        if (vo == null || getClass() != vo.getClass()) return false;
        MovieNetworkLite vthat = (MovieNetworkLite) vo;
        return movieId == vthat.movieId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId);
    }
}
