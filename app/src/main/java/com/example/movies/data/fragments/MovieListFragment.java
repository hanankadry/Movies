package com.example.movies.data.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movies.R;
import com.example.movies.data.adapter.MovieAdapter;
import com.example.movies.data.models.Movie;
import com.example.movies.data.models.MovieResponse;
import com.example.movies.data.service.MovieAPI;
import com.example.movies.databinding.FragmentMovieListBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MovieListFragment extends Fragment implements MovieAdapter.OnClickListener {
    private FragmentMovieListBinding binding;
    private MovieAdapter adapter;
    private List<Movie> list;


    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
        binding = FragmentMovieListBinding.inflate(layoutInflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRetrofit();
        setUpRecycler();
    }

    private void setUpRecycler() {
        adapter = new MovieAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void setUpRetrofit() {
        final String BASE_URL = "https://api.themoviedb.org/3/";
        final String API_KEY = "6557d01ac95a807a036e5e9e325bb3f0";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        MovieAPI movieAPI = retrofit.create(MovieAPI.class);
        Call<MovieResponse> call = movieAPI.getMoviesListFromNetwork(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    list = response.body().getResults();
                    adapter.submitList(list);
                } else {
                    Toast.makeText(requireContext(), "User not authorized", Toast.LENGTH_SHORT).show();
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMovieClicked(Movie movie) {
        NavDirections action = MovieListFragmentDirections.actionMainFragmentToMovieDetailsFragment(
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getVoteCount(),
                movie.getVoteAverage(),
                movie.getOverView(),
                movie.getPosterPath()
        );

        Navigation.findNavController(requireActivity(), R.id.container).navigate(action);
    }
}
