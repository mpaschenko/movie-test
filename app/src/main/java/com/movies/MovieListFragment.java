package com.movies;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.movies.databinding.ListFragmentBinding;
import com.movies.db.entity.MovieEntity;
import com.movies.ui.EndlessRecyclerOnScrollListener;
import com.movies.ui.MoviesAdapter;
import com.movies.ui.RefreshMovieCallback;
import com.movies.viewmodel.MovieListViewModel;

import java.util.List;

/**
 * Created by michaelpaschenko on 9/8/17.
 * Main fragment with list of movies loaded
 */

public class MovieListFragment extends LifecycleFragment implements RefreshMovieCallback {

    public static final String TAG = MovieListFragment.class.getSimpleName();

    private MoviesAdapter mMoviesAdapter;
    private ListFragmentBinding mBinding;
    private MovieListViewModel mViewModel;

    public class MoviesObserver implements Observer<List<MovieEntity>> {
        @Override
        public void onChanged(@Nullable List<MovieEntity> movies) {
            if (movies != null) {
                mMoviesAdapter.setMoviesList(movies);
            }
        }
    }

    /**
     * Common observer for livedata to update movies list adapter
     */
    MoviesObserver mMoviesObserver = new MoviesObserver();

    /**
     * Listener to allow endless loading
     */
    EndlessRecyclerOnScrollListener mScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore() {
            // ask model to load more
            mBinding.swiperefreslayout.setRefreshing(true);
            mViewModel.loadMore(MovieListFragment.this);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);
        mMoviesAdapter = new MoviesAdapter();
        mBinding.recyclerviewMain.setAdapter(mMoviesAdapter);
        mBinding.recyclerviewMain.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mBinding.swiperefreslayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.refreshData(MovieListFragment.this);
            }
        });

        mBinding.recyclerviewMain.addOnScrollListener(mScrollListener);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MovieListViewModel.class);
        applyNoFilter();
    }

    @Override
    public void moviesRefreshComplete() {
        mBinding.swiperefreslayout.setRefreshing(false);
        mScrollListener.setLoading(false);
    }

    /**
     * Apply filter on selected ratings range
     * @param min rating
     * @param max rating
     */
    public void applyRatingsFilter(int min, int max) {
        mViewModel.removeObserver(mMoviesObserver);
        mViewModel.getMoviesWithRatingsFilter(min, max).observe(this, mMoviesObserver);
    }

    /**
     * Apply filter on selected year range
     * @param min year
     * @param max year
     */
    public void applyYearFilter(int min, int max) {
        mViewModel.removeObserver(mMoviesObserver);
        mViewModel.getMoviesWithYearFilter(min, max).observe(this, mMoviesObserver);
    }

    /**
     * Apply filter by genre selected
     * @param genre
     */
    public void applyGenreFilter(String genre) {
        mViewModel.removeObserver(mMoviesObserver);
        mViewModel.getMoviesWithGenreFilter(genre).observe(this, mMoviesObserver);
    }

    /**
     * reset filters
     */
    public void applyNoFilter() {
        mViewModel.removeObserver(mMoviesObserver);
        mViewModel.getMovies().observe(this, mMoviesObserver);

    }
}
