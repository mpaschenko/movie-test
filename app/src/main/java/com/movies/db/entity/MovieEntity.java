package com.movies.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.movies.model.Movie;

import java.util.List;

/**
 * Created by michaelpaschenko on 9/8/17.
 * entity class to store movie instances
 */

@Entity(tableName = "movies")
public class MovieEntity implements Movie {

    @PrimaryKey(autoGenerate = true)
    private int movieId;

    private String title;
    private String image;
    private Double rating;
    private Integer releaseYear;
    private List<String> genre;

    public MovieEntity() {
    }

    public MovieEntity(Movie movie) {
        this.title = movie.getTitle();
        this.image = movie.getImage();
        this.rating = movie.getRating();
        this.releaseYear = movie.getReleaseYear();
        this.genre = movie.getGenre();
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }


    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public Integer getReleaseYear() {
        return releaseYear;
    }


    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }
}
