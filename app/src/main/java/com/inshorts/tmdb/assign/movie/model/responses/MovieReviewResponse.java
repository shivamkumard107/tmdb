package com.inshorts.tmdb.assign.movie.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.inshorts.tmdb.assign.movie.model.Reviews;

public class MovieReviewResponse {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("results")
    @Expose
    private List<Reviews> mReviews;


    public int getPage() {
        return page;
    }

    public List<Reviews> getReviews() {
        return mReviews;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
