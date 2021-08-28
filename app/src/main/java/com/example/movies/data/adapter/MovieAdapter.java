package com.example.movies.data.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies.data.models.Movie;
import com.example.movies.databinding.MovieItemBinding;

public class MovieAdapter extends ListAdapter<Movie, MovieAdapter.MovieViewHolder> {
    private final OnClickListener listener;

    public static DiffUtil.ItemCallback<Movie> movieDiffUtil = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MovieAdapter(OnClickListener listener) {
        super(movieDiffUtil);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MovieItemBinding binding = MovieItemBinding.inflate(layoutInflater, parent, false);
        return new MovieViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        holder.bind(movie, listener);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private MovieItemBinding binding;

        public MovieViewHolder(@NonNull MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Movie movie, OnClickListener listener) {
            final String IMAGE_BASE_URL="https://image.tmdb.org/t/p/w500";

            binding.movieName.setText(movie.getTitle());
            Glide.with(itemView.getContext()).load(IMAGE_BASE_URL + movie.getPosterPath()).into(binding.image);
            binding.clickListener.setOnClickListener(view -> listener.onMovieClicked(movie));
        }
    }

    public interface OnClickListener {
        void onMovieClicked(Movie movie);
    }
}
