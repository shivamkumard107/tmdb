package com.inshorts.tmdb.assign.movie.di;

import com.inshorts.tmdb.assign.movie.ui.main.MainActivity;
import com.squareup.haha.perflib.Main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeAuthActivity();

}
