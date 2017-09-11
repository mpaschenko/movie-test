package com.movies.db.entity;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by michaelpaschenko on 9/8/17.
 * main dao to work with data stored
 */
@Dao
public interface MovieDao {
    @Query("select * from movies")
    LiveData<List<MovieEntity>> load();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MovieEntity> movies);

    @Query("select * from movies where rating >= :min and rating <= :max")
    LiveData<List<MovieEntity>> getWithRatingsFilter(int min, int max);

    @Query("select * from movies where releaseYear >= :min and releaseYear <= :max")
    LiveData<List<MovieEntity>> getWithYearFilter(int min, int max);

    @Query("select releaseYear from movies order by releaseYear asc limit 1")
    int getMinReleaseYear();

    @Query("select releaseYear from movies order by releaseYear desc limit 1")
    int getMaxReleaseYear();

    @Query("select rating from movies order by rating asc limit 1")
    double getMinRating();

    @Query("select rating from movies order by rating desc limit 1")
    double getMaxRating();

    @Query("select genre from movies")
    List<String> getGenres();

    @Query("select * from movies where genre like :genre")
    LiveData<List<MovieEntity>> getWithGenreFilter(String genre);

    @Query("delete from movies")
    void deleteAll();
}
