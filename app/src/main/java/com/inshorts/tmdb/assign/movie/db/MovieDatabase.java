package com.inshorts.tmdb.assign.movie.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.inshorts.tmdb.assign.movie.db.dao.IBookmarkMovieDao;
import com.inshorts.tmdb.assign.movie.model.Movie;

@Database(entities = {Movie.class}, version = 4, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movie_database";
    private static MovieDatabase movieDatabaseInstance;

    public static MovieDatabase getInstance(Context context) {
        if (movieDatabaseInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating DB Instance");
                movieDatabaseInstance = Room
                        .databaseBuilder(context.getApplicationContext(), MovieDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(TAG, "Getting Database Instance");
        return movieDatabaseInstance;
    }

    public abstract IBookmarkMovieDao movieDAO();
}
