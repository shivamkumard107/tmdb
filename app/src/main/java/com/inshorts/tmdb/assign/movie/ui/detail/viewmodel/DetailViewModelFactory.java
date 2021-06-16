package com.inshorts.tmdb.assign.movie.ui.detail.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.inshorts.tmdb.assign.movie.MoviesRepository;


public final class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MoviesRepository mRepository;

    public DetailViewModelFactory(MoviesRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked cast")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(mRepository);
    }
}
