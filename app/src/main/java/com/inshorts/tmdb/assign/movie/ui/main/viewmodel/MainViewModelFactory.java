package com.inshorts.tmdb.assign.movie.ui.main.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.inshorts.tmdb.assign.movie.MoviesRepository;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public final class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MoviesRepository mRepository;

    public MainViewModelFactory(MoviesRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked cast")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(mRepository);
    }
}