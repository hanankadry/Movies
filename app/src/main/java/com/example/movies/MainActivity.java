package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.movies.data.adapter.MovieAdapter;
import com.example.movies.data.models.Movie;
import com.example.movies.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnClickListener {
    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private MovieAdapter adapter;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initViewModel();
        setUpRecycler();
        initObservers();
        initBroadcastReceiver();
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

    private void initBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                binding.refreshBtn.setVisibility(View.VISIBLE);
                binding.refreshBtn.setOnClickListener(view -> {
                    binding.refreshBtn.setVisibility(View.GONE);
                    Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(refresh);
                    overridePendingTransition(0, 0);
                });
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver, new IntentFilter("refresh_movies"));
    }


    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}