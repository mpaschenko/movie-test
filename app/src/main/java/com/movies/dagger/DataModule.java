package com.movies.dagger;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.commonsware.cwac.saferoom.SafeHelperFactory;
import com.movies.App;
import com.movies.BuildConfig;
import com.movies.Encryption;
import com.movies.db.entity.AppDatabase;
import com.movies.network.WebService;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

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

    @Singleton
    @Provides
    AppDatabase getDatabase() {
        try {
            String databaseKey = new Encryption().encrypt(BuildConfig.KEY);

            SafeHelperFactory factory = new SafeHelperFactory(databaseKey.toCharArray());
            return Room.databaseBuilder(application,
                    AppDatabase.class, "movie-database").allowMainThreadQueries().openHelperFactory(factory).build();

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return null;

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
