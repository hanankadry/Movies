package com.example.movies.data.retrofit.service;

import com.example.movies.data.models.Movie;
import com.example.movies.data.models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieAPI {
    @GET("movie/popular")
    Call<MovieResponse> getMoviesListFromNetwork();

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id") int movieId);
}
