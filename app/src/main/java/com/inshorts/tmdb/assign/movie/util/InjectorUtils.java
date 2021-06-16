package com.inshorts.tmdb.assign.movie.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.inshorts.tmdb.assign.movie.MoviesRepository;
import com.inshorts.tmdb.assign.movie.R;
import com.inshorts.tmdb.assign.movie.db.MovieDatabase;
import com.inshorts.tmdb.assign.movie.network.MovieApiServices;
import com.inshorts.tmdb.assign.movie.network.NetworkAdapter;
import com.inshorts.tmdb.assign.movie.ui.detail.viewmodel.DetailViewModelFactory;
import com.inshorts.tmdb.assign.movie.ui.main.viewmodel.MainViewModelFactory;
import com.inshorts.tmdb.assign.movie.util.threads.AppExecutors;


public final class InjectorUtils {

    private static MoviesRepository provideRepository(Context context, SharedPreferences.OnSharedPreferenceChangeListener changeListener) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MovieApiServices vApiServices = NetworkAdapter
                .getRetrofitInstance()
                .create(MovieApiServices.class);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sp.getString(context.getString(R.string.pref_language_key), "");
        sp.registerOnSharedPreferenceChangeListener(changeListener);
        return MoviesRepository.getInstance(database.movieDAO(), vApiServices, executors, language);
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context, SharedPreferences.OnSharedPreferenceChangeListener changeListener) {
        MoviesRepository repository = provideRepository(context.getApplicationContext(), changeListener);
        return new MainViewModelFactory(repository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, SharedPreferences.OnSharedPreferenceChangeListener changeListener) {
        MoviesRepository repository = provideRepository(context.getApplicationContext(), changeListener);
        return new DetailViewModelFactory(repository);
    }
}
