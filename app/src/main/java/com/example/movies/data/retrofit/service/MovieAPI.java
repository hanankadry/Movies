package com.example.movies.data.retrofit.service;

import com.example.movies.data.models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieAPI {
    @GET("movie/popular")
    Call<MovieResponse> getMoviesListFromNetwork();
}
