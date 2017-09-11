package com.movies.db.entity;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by michaelpaschenko on 9/10/17.
 * helper converter to allow store list of the strings in the database, by concatenatinf / split
 */

public class GenresConverter  {
    @TypeConverter
    public List<String> storedStringToGenres(String value) {
        List<String> genres = Arrays.asList(value.split("\\s*,\\s*"));
        return genres;
    }

    @TypeConverter
    public String genresToStoredString(List<String> strings) {
        String value = "";

        for (String lang : strings)
            value += lang + ",";

        return value;
    }
}
