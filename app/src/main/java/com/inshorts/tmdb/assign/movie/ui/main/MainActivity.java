package com.inshorts.tmdb.assign.movie.ui.main;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.inshorts.tmdb.assign.movie.R;
import com.inshorts.tmdb.assign.movie.model.Category;
import com.inshorts.tmdb.assign.movie.model.IMovie;
import com.inshorts.tmdb.assign.movie.model.Movie;
import com.inshorts.tmdb.assign.movie.model.MovieNetworkLite;
import com.inshorts.tmdb.assign.movie.model.responses.MovieListResponse;
import com.inshorts.tmdb.assign.movie.ui.detail.MovieDetailActivity;
import com.inshorts.tmdb.assign.movie.ui.main.adapters.MoviesAdapter;
import com.inshorts.tmdb.assign.movie.ui.main.callbacks.BookmarkMoviesCallback;
import com.inshorts.tmdb.assign.movie.ui.main.listeners.CategoryMenuListener;
import com.inshorts.tmdb.assign.movie.ui.main.listeners.SearchQueryListener;
import com.inshorts.tmdb.assign.movie.ui.main.viewmodel.MainViewModel;
import com.inshorts.tmdb.assign.movie.ui.main.viewmodel.MainViewModelFactory;
import com.inshorts.tmdb.assign.movie.util.InjectorUtils;
import com.inshorts.tmdb.assign.movie.util.NetworkUtils;
import com.inshorts.tmdb.assign.movie.util.ViewUtils;

import static com.inshorts.tmdb.assign.movie.util.Constants.KEY_MOVIE_ID;
import static com.inshorts.tmdb.assign.movie.util.Constants.KEY_MOVIE_IS_FAVOURITE;
import static com.inshorts.tmdb.assign.movie.util.Constants.KEY_MOVIE_POSTER;

public class MainActivity extends AppCompatActivity implements BookmarkMoviesCallback,
        MoviesAdapter.IMovieClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String TAG = "MainActivity";
    private final String KEY_APPBAR_TITLE_PERSISTENCE = "movie_category";
    private final String KEY_MOVIE_LIST_PERSISTENCE = "movie_list";
    private final String KEY_FAV_MOVIE_LIST_PERSISTENCE = "fav_movie_list";
    private MovieListResponse mMovieList;
    private List<Movie> bookmarkMovies;
    private MainViewModel mMainViewModel;
    private SearchView searchView;

    @BindView(R.id.progressbar_movies_loading)
    ProgressBar pbLoadMovies;
    @BindView(R.id.recycler_view_movies)
    RecyclerView rvMovies;
    @BindView(R.id.text_view_info_message)
    TextView tvInfoMessage;
    @BindView(R.id.text_view_bookmarks_message)
    TextView tvFavMessage;


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: " + searchView.isIconified());
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Toolbar vTbMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(vTbMain);
        MainViewModelFactory vMainViewModelFactory = InjectorUtils.provideMainViewModelFactory(this, this);
        mMainViewModel = ViewModelProviders.of(this, vMainViewModelFactory).get(MainViewModel.class);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIE_LIST_PERSISTENCE)) {
            mMovieList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_LIST_PERSISTENCE));
            setTitle(savedInstanceState.getCharSequence(KEY_APPBAR_TITLE_PERSISTENCE));
            bindNetworkMovies(mMovieList.getMoviesResult());
        } else if (savedInstanceState != null && savedInstanceState.containsKey(KEY_FAV_MOVIE_LIST_PERSISTENCE)) {
            bookmarkMovies = Parcels.unwrap(savedInstanceState.getParcelable(KEY_FAV_MOVIE_LIST_PERSISTENCE));
            setTitle(savedInstanceState.getCharSequence(KEY_APPBAR_TITLE_PERSISTENCE));
            bindBookmarkMovies(bookmarkMovies);
        } else if (NetworkUtils.isOnline(this)) {
            removeMessageInfo(tvInfoMessage);
            loadMovies(Category.NOW_PLAYING, 1);
        }
    }

    private void bindBookmarkMovies(List<Movie> bookmarkMovies) {
        if (bookmarkMovies.isEmpty()) {
            removeMessageInfo(tvInfoMessage);
            showDefaultFavMessage();
        } else {
            MoviesAdapter vMoviesAdapter = new MoviesAdapter(null, bookmarkMovies, this);
            setupRecyclerView(vMoviesAdapter);
        }
    }

    private void setupRecyclerView(MoviesAdapter moviesAdapter) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),
                ViewUtils.calculateNoOfColumns(this));
        ViewUtils.setupRecyclerView(rvMovies, gridLayoutManager, this);
        rvMovies.setAdapter(moviesAdapter);
        pbLoadMovies.setVisibility(View.GONE);
    }

    private void removeMessageInfo(TextView tvInfoMessage) {
        if (tvInfoMessage.getVisibility() == View.VISIBLE)
            tvInfoMessage.setVisibility(View.GONE);
    }

    @Override
    public void loadBookmarkMovies() {
        mMainViewModel.getBookmarkMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieList = null;
                if (movies != null) {
                    bookmarkMovies = movies;
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(getString(R.string.action_sort_bookmarks));
                    bindBookmarkMovies(bookmarkMovies);
                }
            }
        });
    }

    @Override
    public void loadMovies(final Category category, int page) {
        mMainViewModel.getMovies(category, page).observe(this, new Observer<MovieListResponse>() {
            @Override
            public void onChanged(@Nullable MovieListResponse movies) {
                setAppBarTitle(category);
                mMovieList = null;
                if (movies != null) {
                    mMovieList = movies;
                    Log.d(TAG, "onChanged: " + mMovieList.toString());
                    bindNetworkMovies(mMovieList.getMoviesResult());
                }
            }
        });
    }

    @Override
    public void loadSearchedQuery(Category category, int page, String query) {
        mMainViewModel.getSearchedMovies(category, page, query).observe(this, new Observer<MovieListResponse>() {
            @Override
            public void onChanged(@Nullable MovieListResponse movies) {
                mMovieList = null;
                if (movies != null) {
                    mMovieList = movies;
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(getString(R.string.action_sort_search));
                    Log.d(TAG, "onChanged: " + mMovieList.toString());
                    bindNetworkMovies(mMovieList.getMoviesResult());
                }
            }
        });
    }

    private void setAppBarTitle(Category category) {
        if (getSupportActionBar() != null)
            switch (category) {
                case TRENDING:
                    getSupportActionBar().setTitle(getString(R.string.action_sort_top_rated));
                    break;
                case NOW_PLAYING:
                    getSupportActionBar().setTitle(getString(R.string.action_sort_now_playing));
                    break;
                case SEARCH:
                    getSupportActionBar().setTitle(getString(R.string.action_sort_search));
                    break;
            }
    }

    void bindNetworkMovies(List<MovieNetworkLite> movies) {
        if (movies != null) {
            Log.d(TAG, "bindNetworkMovies: " + movies.toString());
            MoviesAdapter vMoviesAdapter = new MoviesAdapter(movies, null, this);
            setupRecyclerView(vMoviesAdapter);
        } else {
            Log.d(TAG, "bindNetworkMovies: " + "movie null");
            showNoConnectionMessage();
        }
    }

    private void showNoConnectionMessage() {
        rvMovies.setVisibility(View.GONE);
        tvInfoMessage.setVisibility(View.VISIBLE);
        pbLoadMovies.setVisibility(View.GONE);
    }

    private void showDefaultFavMessage() {
        rvMovies.setVisibility(View.GONE);
        pbLoadMovies.setVisibility(View.GONE);
        tvFavMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovieList != null)
            outState.putParcelable(KEY_MOVIE_LIST_PERSISTENCE, Parcels.wrap(mMovieList));
        if (bookmarkMovies != null)
            outState.putParcelable(KEY_FAV_MOVIE_LIST_PERSISTENCE, Parcels.wrap(bookmarkMovies));
        if (getSupportActionBar() != null)
            outState.putCharSequence(KEY_APPBAR_TITLE_PERSISTENCE, getSupportActionBar().getTitle());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView = (SearchView) search.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.clearFocus();
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchQueryListener(mMainViewModel, this));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search) {

        } else if (itemId == R.id.action_sort) {
            showSortPopUpMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortPopUpMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.action_sort));
        sortMenu.setOnMenuItemClickListener(new CategoryMenuListener(mMainViewModel, this));
        sortMenu.inflate(R.menu.category_menu);
        sortMenu.show();
    }

    @Override
    public void viewMovieDetails(IMovie movie, ImageView view, boolean isBookmark) {
        Intent vIntent = new Intent(this, MovieDetailActivity.class);
        vIntent.putExtra(KEY_MOVIE_ID, movie.getMovieId());
        vIntent.putExtra(KEY_MOVIE_POSTER, movie.getMoviePoster());
        vIntent.putExtra(KEY_MOVIE_IS_FAVOURITE, isBookmark);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivity(vIntent, options.toBundle());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_language_key))) {
//            FIXME Change Language of request
            String language = sharedPreferences.getString(key, getResources().getString(R.string.pref_language_val_english));
            Language.setUpLocale(language, this);
//            new MovieListAsync(mMainViewModel, Category.NOW_PLAYING, this).execute();
            loadMovies(Category.NOW_PLAYING, 1);
        }
    }
}
