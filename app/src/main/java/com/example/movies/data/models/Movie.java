package com.example.movies.data.models;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class Movie implements Serializable {
    private int id;
    @Json(name = "poster_path")
    private String posterPath;
    @Json(name = "overview")
    private String overView;
    @Json(name = "release_date")
    private String releaseDate;
    @Json(name = "vote_average")
    private Float voteAverage;
    @Json(name = "vote_count")
    private int voteCount;
    private String title;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
