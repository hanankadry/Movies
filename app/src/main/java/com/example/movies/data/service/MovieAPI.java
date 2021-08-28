package com.example.movies.data.service;

import com.example.movies.data.models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieAPI {
    @GET("movie/popular")
    Call<MovieResponse> getMoviesListFromNetwork(
            @Query("api_key")
            String apiKey
    );
}
