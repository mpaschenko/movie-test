package com.movies.network;

import com.movies.model.MovieWeb;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by michaelpaschenko on 9/8/17.
 * retrofit interface description
 */

public interface WebService {
    @GET("/json/movies.json")
    Call<List<MovieWeb>> getMovies();
}
