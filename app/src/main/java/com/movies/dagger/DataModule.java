package com.movies.dagger;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.movies.App;
import com.movies.BuildConfig;
import com.movies.db.entity.AppDatabase;
import com.movies.db.entity.MovieDao;
import com.movies.network.WebService;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by michaelpaschenko on 9/8/17.
 * main dagger module
 */

@Module
public class DataModule {
    private final Application application;

    public DataModule(App app) {
        this.application = app;
    }

    @Provides
    Context getContext() {
        return application;
    }


    @Provides
    AppDatabase getDatabase() {
        return Room.databaseBuilder(application,
                AppDatabase.class, "movie-database").allowMainThreadQueries().build();
    }

    @Provides
    @Singleton
    public WebService getWebService() {

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebService.class);
    }
}
