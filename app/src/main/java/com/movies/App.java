package com.movies;

import android.app.Application;

import com.movies.dagger.AppComponent;
import com.movies.dagger.DaggerAppComponent;
import com.movies.dagger.DataModule;

/**
 * Created by michaelpaschenko on 9/8/17.
 * main application class
 */

public class App extends Application {
    private static App application;
    private AppComponent component;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        component = DaggerAppComponent.builder()
                .dataModule(new DataModule(this))
                .build();
    }

    public static AppComponent getComponent() {
        return application.component;
    }
}
