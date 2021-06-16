package com.inshorts.tmdb.assign.movie.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.inshorts.tmdb.assign.movie.model.Movie;

@Dao
public interface IBookmarkMovieDao {

    @Query("SELECT * FROM bookmark_movie")
    LiveData<List<Movie>> loadAll();

    @Insert
    void addToBookmarks(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void removeFromBookmarks(Movie movie);

    @Query("SELECT * FROM bookmark_movie WHERE movie_id = :id")
    LiveData<Movie> loadById(int id);

    @Query("SELECT * FROM bookmark_movie WHERE movie_id = :id AND movie_title = :title")
    Movie checkForFavs(int id, String title);

}
