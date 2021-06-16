package com.inshorts.tmdb.assign.movie.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

import com.inshorts.tmdb.assign.movie.model.MovieNetworkLite;

@Parcel(Parcel.Serialization.BEAN)
public class MovieListResponse {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("results")
    @Expose
    private List<MovieNetworkLite> moviesResult;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<MovieNetworkLite> getMoviesResult() {
        return moviesResult;
    }


}
