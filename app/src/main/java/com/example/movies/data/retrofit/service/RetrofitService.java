package com.example.movies.data.retrofit.service;

import com.example.movies.data.retrofit.interceptor.AuthenticationInterceptor;
import com.example.movies.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitService {

    private static final OkHttpClient.Builder client = new OkHttpClient
            .Builder()
            .addNetworkInterceptor(new AuthenticationInterceptor());

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client.build())
            .addConverterFactory(MoshiConverterFactory.create());

    private static final Retrofit retrofit = builder.build();

    public static MovieAPI movieAPI = retrofit.create(MovieAPI.class);

    public static MovieAPI movieApi(){
        return movieAPI;
    }
}