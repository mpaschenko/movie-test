package com.movies.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.movies.R;

import com.movies.databinding.MovieItemBinding;
import com.movies.model.Movie;

import java.util.List;
import java.util.Objects;

/**
 * Created by michaelpaschenko on 9/8/17.
 * movie adapter for recyclerview
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<? extends Movie> mMoviesList;

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MovieItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_item,
                        parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.binding.setMovie(mMoviesList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mMoviesList == null ? 0 : mMoviesList.size();
    }

    public void setMoviesList(final List<? extends Movie> moviesList) {
        this.mMoviesList = moviesList;
        this.notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        final MovieItemBinding binding;
        public MovieViewHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
