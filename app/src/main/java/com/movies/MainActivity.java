package com.movies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.movies.ui.RadioDialogFragment;
import com.movies.ui.RangeDialogFragment;
import com.movies.viewmodel.MovieListViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RangeDialogFragment.RangeFilterListener, RadioDialogFragment.RadioFilterListener {

    MovieListFragment fragment;

    private static final int KEY_FILTER_BY_RATING = 256;
    private static final int KEY_FILTER_BY_RELEASE_YEAR = 512;
    private static final int KEY_FILTER_BY_GENRE = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            fragment = new MovieListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, MovieListFragment.TAG).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MovieListViewModel viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);

        int id = item.getItemId();
        if (id == R.id.action_filter_by_rating) {
            int minRating = (int) viewModel.getMinRating();
            int maxRating = (int) viewModel.getMaxRating();
            RangeDialogFragment.newInstance(minRating, maxRating, "Filter by rating", KEY_FILTER_BY_RATING).show(getFragmentManager(), RangeDialogFragment.TAG);
        }
        if (id == R.id.action_filter_by_release_year) {
            int minYear = viewModel.getMinReleaseYear();
            int maxYear = viewModel.getMaxReleaseYear();
            RangeDialogFragment.newInstance(minYear, maxYear, "Filter by release year", KEY_FILTER_BY_RELEASE_YEAR).show(getFragmentManager(), RangeDialogFragment.TAG);
        }
        if (id == R.id.action_filter_by_genre) {
            ArrayList<String> genres = viewModel.getAvailableGenres();
            RadioDialogFragment.newInstance(genres, "Filter by genre", KEY_FILTER_BY_GENRE).show(getFragmentManager(), RadioDialogFragment.TAG);
        }
        if (id == R.id.action_no_filter) {
            fragment.applyNoFilter();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFilter(String genre, int tag) {
        fragment.applyGenreFilter(genre);
    }

    @Override
    public void onFilter(int min, int max, int tag) {
        switch (tag) {
            case KEY_FILTER_BY_RATING:
                fragment.applyRatingsFilter(min, max);
                break;
            case KEY_FILTER_BY_RELEASE_YEAR:
                fragment.applyYearFilter(min, max);
                break;
        }

    }
}
