package com.movies.db.entity;

/**
 * Created by michaelpaschenko on 9/8/17.
 * room database to store offline data
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {MovieEntity.class}, version = 1)
@TypeConverters({GenresConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDao();
}
