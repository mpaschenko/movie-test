package com.movies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.ArraySet;

import com.movies.App;
import com.movies.MovieListFragment;
import com.movies.db.entity.GenresConverter;
import com.movies.db.entity.MovieEntity;
import com.movies.db.entity.MovieRepository;
import com.movies.ui.RefreshMovieCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by michaelpaschenko on 9/8/17.
 * viewmodel which operates with data
 */

public class MovieListViewModel extends AndroidViewModel {

    @Inject
    MovieRepository mMovieRepo;

    private LiveData<List<MovieEntity>> mObservableMovies;

    public MovieListViewModel(Application application) {
        super(application);

        App.getComponent().inject(this);

        mObservableMovies = mMovieRepo.getMovies();
    }

    public LiveData<List<MovieEntity>> getMovies() {
        mObservableMovies = mMovieRepo.getMovies();
        return mObservableMovies;
    }

    public void refreshData(RefreshMovieCallback callback) {
        mMovieRepo.refreshMovies(callback);
    }

    public LiveData<List<MovieEntity>> getMoviesWithRatingsFilter(int min, int max) {
        mObservableMovies = mMovieRepo.getMoviesWithRatingsFilter(min, max);
        return mObservableMovies;
    }

    public LiveData<List<MovieEntity>> getMoviesWithYearFilter(int min, int max) {
        mObservableMovies = mMovieRepo.getMoviesWithYearFilter(min, max);
        return mObservableMovies;
    }

    public LiveData<List<MovieEntity>> getMoviesWithGenreFilter(String genre) {
        mObservableMovies = mMovieRepo.getMoviesWithGenreFilter(genre);
        return mObservableMovies;
    }

    public void removeObserver(MovieListFragment.MoviesObserver observer) {
        mObservableMovies.removeObserver(observer);
    }

    public int getMinReleaseYear() {
        return mMovieRepo.getMinReleaseYear();
    }

    public int getMaxReleaseYear() {
        return mMovieRepo.getMaxReleaseYear();
    }

    public double getMinRating() {
        return mMovieRepo.getMinRating();
    }

    public double getMaxRating() {
        return mMovieRepo.getMaxRating();
    }

    public ArrayList<String> getAvailableGenres() {
        Set<String> genres = new HashSet<>();

        List<String> movies = mMovieRepo.getGenres();
        GenresConverter converter = new GenresConverter();
        for(String genre : movies) {
            genres.addAll( converter.storedStringToGenres(genre));
        }

        return new ArrayList<>(genres);
    }

    public void loadMore(RefreshMovieCallback callback) {
        mMovieRepo.loadMore(callback);
    }
}
