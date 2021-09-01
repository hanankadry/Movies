package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movies.data.models.Movie;
import com.example.movies.databinding.ActivityMovieDetailsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;
    private BroadcastReceiver broadcastReceiver;
    private MovieDetailsViewModel movieDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        initViewModel();
        initObserver();

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        setMovieData(movie);
    }

    private void initViewModel() {
        movieDetailsViewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);
    }

    private void initObserver() {
        movieDetailsViewModel.movieMutableLiveData.observe(this, this::setMovieData);

        movieDetailsViewModel.errorMutableLiveData.observe(this,
                msg -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    private void setMovieData(Movie movie) {
        initBroadcastReceiver(movie);

        if (movie != null) {
            final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat formatPattern = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            String outputDate = null;
            try {
                Date inputDate = inputFormat.parse(movie.getReleaseDate());
                if (inputDate != null)
                    outputDate = formatPattern.format(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.movieName.setText(movie.getTitle());
            binding.overview.setText(movie.getOverView());
            Glide.with(this).load(IMAGE_BASE_URL + movie.getPosterPath()).into(binding.posterImage);
            binding.voteAverage.setText(Float.toString(movie.getVoteAverage()));
            binding.voteCount.setText(Integer.toString(movie.getVoteCount()));
            binding.releaseDate.setText(outputDate);
        }
    }

    private void initBroadcastReceiver(Movie movie) {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (movie.getId() == Integer.parseInt(extras.getString("action_id"))) {
                    binding.layout.setVisibility(View.GONE);
                    binding.layout.postDelayed(() -> binding.layout.setVisibility(View.VISIBLE), 2000);
                } else {
                    movieDetailsViewModel.getMovieDetails(Integer.parseInt(extras.getString("action_id")));
                }
            }
        };

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver, new IntentFilter("movie_details"));
    }

}