package com.example.movies.data.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movies.MainActivity;
import com.example.movies.MainViewModel;
import com.example.movies.R;
import com.example.movies.data.adapter.MovieAdapter;
import com.example.movies.data.models.Movie;
import com.example.movies.data.models.MovieResponse;
import com.example.movies.data.retrofit.service.MovieAPI;
import com.example.movies.databinding.FragmentMovieListBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MovieListFragment extends Fragment implements MovieAdapter.OnClickListener {
    private FragmentMovieListBinding binding;
    private MainViewModel mainViewModel;
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
        setUpRecycler();
        initViewModel();
        initObservers();
    }

    private void setUpRecycler() {
        adapter = new MovieAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void initObservers() {
        mainViewModel.moviesMutableLiveData.observe(getViewLifecycleOwner(), movieList -> {
            binding.progressBar.setVisibility(View.GONE);
            adapter.submitList(movieList);
        });
        mainViewModel.errorMutableLiveData.observe(getViewLifecycleOwner(), msg -> {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
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
