package com.example.movies;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movies.data.models.Movie;
import com.example.movies.data.retrofit.service.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsViewModel extends ViewModel {

    public MutableLiveData<Movie> movieMutableLiveData = new MutableLiveData();
    public MutableLiveData<String> errorMutableLiveData = new MutableLiveData();

    public void getMovieDetails(int movieId) {
        Call<Movie> call =
                RetrofitService
                        .movieApi()
                        .getMovieDetails(movieId);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()){
                    Movie movie = response.body();
                    movieMutableLiveData.postValue(movie);
                } else {
                    errorMutableLiveData.postValue("User not authorized");
                }

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                errorMutableLiveData.postValue(t.getMessage());
            }
        });


    }
}
