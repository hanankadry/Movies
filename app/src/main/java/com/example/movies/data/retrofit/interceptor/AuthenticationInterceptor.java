package com.example.movies.data.retrofit.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.movies.utils.Constants.API_KEY;

public class AuthenticationInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", API_KEY).build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
