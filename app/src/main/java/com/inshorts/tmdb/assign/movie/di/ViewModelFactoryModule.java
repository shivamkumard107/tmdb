package com.inshorts.tmdb.assign.movie.di;

import androidx.lifecycle.ViewModelProvider;

import com.inshorts.tmdb.assign.movie.ui.main.viewmodel.MainViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {
    @Binds
    public abstract ViewModelProvider.Factory  bindViewModelFactory(MainViewModelFactory mainViewModelFactory);

}
