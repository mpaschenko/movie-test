package com.movies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by michaelpaschenko on 9/8/17.
 * interface to describe movie object
 */

public interface Movie {
    String getTitle();
    String getImage();
    Double getRating();
    Integer getReleaseYear();
    List<String> getGenre();
}