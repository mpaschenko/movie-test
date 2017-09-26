package com.movies.db.entity;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.movies.model.Movie;
import com.movies.model.MovieWeb;
import com.movies.network.WebService;
import com.movies.ui.RefreshMovieCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by michaelpaschenko on 9/8/17.
 * repository class which can load data online and store it in the database
 */

@Singleton
public class MovieRepository {

    private static final String TAG = MovieRepository.class.getSimpleName();

    private final WebService mWebService;
    private final MovieDao mMovieDao;
    private final AppDatabase mDatabase;

    @Inject
    public MovieRepository(WebService webService, AppDatabase database) {
        mWebService = webService;
        mMovieDao = database.getMovieDao();
        mDatabase = database;
    }

    public LiveData<List<MovieEntity>> getMovies() {
        return mMovieDao.load();
    }

    public List<String> getGenres() {
        return mMovieDao.getGenres();
    }

    public LiveData<List<MovieEntity>> getMoviesWithRatingsFilter(int min, int max) {
        return mMovieDao.getWithRatingsFilter(min, max);
    }

    public LiveData<List<MovieEntity>> getMoviesWithYearFilter(int min, int max) {
        return mMovieDao.getWithYearFilter(min, max);
    }

    public LiveData<List<MovieEntity>> getMoviesWithGenreFilter(String genre) {
        return mMovieDao.getWithGenreFilter('%' + genre + '%');
    }

    public int getMinReleaseYear() {
        return mMovieDao.getMinReleaseYear();
    }

    public int getMaxReleaseYear() {
        return mMovieDao.getMaxReleaseYear();
    }

    public double getMinRating() {
        return mMovieDao.getMinRating();
    }

    public double getMaxRating() {
        return mMovieDao.getMaxRating();
    }


    public void refreshMovies(final RefreshMovieCallback callback) {
        performNetworkCall(callback, true);
    }

    public void loadMore(final RefreshMovieCallback callback) {
        performNetworkCall(callback, false);
    }

    private void performNetworkCall(final RefreshMovieCallback callback, final boolean clearData) {
        Log.d(TAG, "performNetworkCall");
        mWebService.getMovies().enqueue(new Callback<List<MovieWeb>>() {
            @Override
            public void onResponse(Call<List<MovieWeb>> call, Response<List<MovieWeb>> response) {
                List<MovieEntity> movies = new ArrayList<>();
                for (Movie movie : response.body()) {
                    movies.add(new MovieEntity(movie));
                }

                mDatabase.beginTransaction();
                try {
                    // clear database
                    if (clearData) {
                        mMovieDao.deleteAll();
                    }
                    // save to database
                    mMovieDao.insertAll(movies);
                    mDatabase.setTransactionSuccessful();
                } finally {
                    mDatabase.endTransaction();
                }

                callback.moviesRefreshComplete();
            }

            @Override
            public void onFailure(Call<List<MovieWeb>> call, Throwable t) {
                t.printStackTrace();
                callback.moviesRefreshComplete();
            }
        });
    }
}
