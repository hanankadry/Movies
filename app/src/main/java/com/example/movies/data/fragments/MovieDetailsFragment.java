package com.example.movies.data.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.data.models.Movie;
import com.example.movies.databinding.FragmentMovieDetailsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDetailsFragment extends Fragment {
    private FragmentMovieDetailsBinding binding;
    private MovieDetailsFragmentArgs args;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false);

        args = MovieDetailsFragmentArgs.fromBundle(getArguments());

        final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat formatPattern = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String outputDate = null;
        try {
            Date inputDate = inputFormat.parse(args.getReleaseDate());
            if (inputDate != null)
                outputDate = formatPattern.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//
        binding.movieName.setText(args.getName());
        binding.overview.setText(args.getOverview());
        Glide.with(this).load(IMAGE_BASE_URL + args.getPoster()).into(binding.posterImage);
        binding.voteAverage.setText(Float.toString(args.getAverage()));
        binding.voteCount.setText(Integer.toString(args.getCount()));
        binding.releaseDate.setText(outputDate);

        return binding.getRoot();
    }
}