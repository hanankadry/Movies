package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        initObservers();
    }

    private void initObservers(){
        Movie movie = (Movie) getIntent().getSerializableExtra("MOVIE");

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
    private void initBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver, new IntentFilter("movie_details"));
    }

}