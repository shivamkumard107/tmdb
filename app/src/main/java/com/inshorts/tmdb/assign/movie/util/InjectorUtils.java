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

    private static MoviesRepository provideRepository(Context context) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MovieApiServices vApiServices = NetworkAdapter
                .getRetrofitInstance()
                .create(MovieApiServices.class);
        String language = "en-US"; //used default for now
        return MoviesRepository.getInstance(database.movieDAO(), vApiServices, executors, language);
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context) {
        MoviesRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context) {
        MoviesRepository repository = provideRepository(context.getApplicationContext());
        return new DetailViewModelFactory(repository);
    }
}
