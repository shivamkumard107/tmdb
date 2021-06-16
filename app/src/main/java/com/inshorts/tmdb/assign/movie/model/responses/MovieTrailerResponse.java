package com.inshorts.tmdb.assign.movie.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.inshorts.tmdb.assign.movie.model.Trailers;

public class MovieTrailerResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private List<Trailers> moviesResult;

    public int getId() {
        return id;
    }

    public List<Trailers> getTrailers() {
        return moviesResult;
    }
}
