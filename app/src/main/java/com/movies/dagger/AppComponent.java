package com.movies.dagger;

import com.movies.viewmodel.MovieListViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by michaelpaschenko on 9/8/17.
 * main dagger component
 */

@Singleton
@Component(
        modules = {
                DataModule.class
        }
)
public interface AppComponent {
    void inject(MovieListViewModel movieListViewModel);
}
