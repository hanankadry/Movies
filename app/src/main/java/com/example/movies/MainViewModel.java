package com.example.movies;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movies.data.models.Movie;
import com.example.movies.data.models.MovieResponse;
import com.example.movies.data.retrofit.service.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    public MutableLiveData<List<Movie>> moviesMutableLiveData = new MutableLiveData();
    public MutableLiveData<String> errorMutableLiveData = new MutableLiveData();

    public MainViewModel() {
        getData();
    }

    private void getData(){
        Call<MovieResponse> call =
                RetrofitService
                        .movieApi()
                        .getMoviesListFromNetwork();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()){
                    List<Movie> list = response.body().getResults();
                    moviesMutableLiveData.postValue(list);
                } else {
                    errorMutableLiveData.postValue("User not authorized");
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                errorMutableLiveData.postValue(t.getMessage());
            }
        });

    }

}