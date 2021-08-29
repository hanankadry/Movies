package com.example.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.movies.data.adapter.MovieAdapter;
import com.example.movies.data.models.Movie;
import com.example.movies.data.models.MovieResponse;
import com.example.movies.data.retrofit.service.MovieAPI;
import com.example.movies.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnClickListener {
    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private MovieAdapter adapter;
    private List<Movie> list;
    private static final String TAG = "Lecture6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initViewModel();
        setUpRecycler();
        initObservers();
        initializeNotifications();
    }

    private void initializeNotifications() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();

                    Log.d(TAG, "token = " + token);
                    Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                });
    }

    private void setUpRecycler() {
        adapter = new MovieAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void initObservers() {
        mainViewModel.moviesMutableLiveData.observe(this, movieList -> {
            binding.progressBar.setVisibility(View.GONE);
            adapter.submitList(movieList);
        });
        mainViewModel.errorMutableLiveData.observe(this, msg -> {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}