package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.movies.data.models.Movie;
import com.example.movies.databinding.ActivityMovieDetailsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {
    ActivityMovieDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

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
}