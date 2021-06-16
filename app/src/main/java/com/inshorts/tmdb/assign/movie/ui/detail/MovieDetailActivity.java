package com.inshorts.tmdb.assign.movie.ui.detail;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.inshorts.tmdb.assign.movie.R;
import com.inshorts.tmdb.assign.movie.model.Movie;
import com.inshorts.tmdb.assign.movie.model.Reviews;
import com.inshorts.tmdb.assign.movie.model.Trailers;
import com.inshorts.tmdb.assign.movie.ui.detail.adapters.IShareTrailerHandler;
import com.inshorts.tmdb.assign.movie.ui.detail.adapters.IWatchTrailerClickHandler;
import com.inshorts.tmdb.assign.movie.ui.detail.adapters.ReviewsAdapter;
import com.inshorts.tmdb.assign.movie.ui.detail.adapters.TrailersAdapter;
import com.inshorts.tmdb.assign.movie.ui.detail.async.MovieDetailsAsyncTask;
import com.inshorts.tmdb.assign.movie.ui.detail.async.MovieDetailsCallBack;
import com.inshorts.tmdb.assign.movie.ui.detail.viewmodel.DetailViewModel;
import com.inshorts.tmdb.assign.movie.ui.detail.viewmodel.DetailViewModelFactory;
import com.inshorts.tmdb.assign.movie.util.Constants;
import com.inshorts.tmdb.assign.movie.util.InjectorUtils;
import com.inshorts.tmdb.assign.movie.util.threads.AppExecutors;

import static com.inshorts.tmdb.assign.movie.util.Constants.BACKDROP_BASE_URL;
import static com.inshorts.tmdb.assign.movie.util.Constants.KEY_MOVIE_ID;
import static com.inshorts.tmdb.assign.movie.util.Constants.KEY_MOVIE_IS_FAVOURITE;
import static com.inshorts.tmdb.assign.movie.util.Constants.KEY_MOVIE_POSTER;
import static com.inshorts.tmdb.assign.movie.util.Constants.POSTER_BASE_URL;
import static com.inshorts.tmdb.assign.movie.util.PaletteExtractorUtil.getBitmapFromUrl;
import static com.inshorts.tmdb.assign.movie.util.PaletteExtractorUtil.getDarkVibrantColor;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailsCallBack, SharedPreferences.OnSharedPreferenceChangeListener {
    private final String KEY_MOVIE_PERSISTENCE = "movie";
    @BindView(R.id.toolbar_details)
    Toolbar mToolbar;
    @BindView(R.id.iv_poster_image_details)
    ImageView vPosterImage;
    @BindView(R.id.backdrop_image_view)
    ImageView mBackdropImageView;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout vCollapsingToolbarLayout;
    @BindView(R.id.text_view_movie_title)
    TextView tvMovieTitle;
    @BindView(R.id.rating_bar_movie_avg)
    RatingBar rbMovieRating;
    @BindView(R.id.text_view_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.text_view_overview)
    TextView tvOverview;
    @BindView(R.id.recycler_view_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.recycler_view_trailers)
    RecyclerView rvTrailers;
    @BindView(R.id.tv_no_review)
    TextView tvNoReview;
    @BindView(R.id.tv_no_trailer)
    TextView tvNoTrailer;
    @BindView(R.id.image_view_bookmarks)
    ImageView ivBookmarks;
    @BindView(R.id.tShare)
    ImageView tShare;

    private int mMovieId;
    private String mMoviePoster;
    private Movie mMovie;
    private DetailViewModel detailViewModel;
    private boolean mIsBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        supportPostponeEnterTransition();
        setupToolBar();
        vPosterImage.setTransitionName("poster");

        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (intent.getExtras() != null) {
            mMovieId = intent.getExtras().getInt(KEY_MOVIE_ID, -1);
            mMoviePoster = intent.getStringExtra(KEY_MOVIE_POSTER);
            mIsBookmark = intent.getBooleanExtra(KEY_MOVIE_IS_FAVOURITE, false);
        }
        DetailViewModelFactory vDetailViewModelFactory = InjectorUtils.provideDetailViewModelFactory(this);
        detailViewModel = ViewModelProviders.of(this, vDetailViewModelFactory).get(DetailViewModel.class);
        checkIfBookmark(mMovieId);
        if (uri != null) {
            String param = uri.getLastPathSegment();
            if (param != null) {
                mMovieId = Integer.parseInt(param.trim());
                Toast.makeText(this, param, Toast.LENGTH_SHORT).show();
                new MovieDetailsAsyncTask(detailViewModel, this).execute(mMovieId);
            }
        } else {
            if (savedInstanceState != null) {
                mMovie = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_PERSISTENCE));
                complete(mMovie);
            } else {
                if (!mIsBookmark) {
                    new MovieDetailsAsyncTask(detailViewModel, this).execute(mMovieId);
                } else {
                    loadMovieDetailsFromCache();
                }
            }
        }
        tShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMovieDetails();
            }
        });
    }

    private void shareMovieDetails() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, read about this awesome movie I watched today! \n" + Constants.SHARE_BASE_URL + mMovieId);
        startActivity(shareIntent);
    }

    private void loadMovieDetailsFromCache() {
        detailViewModel.getFav(mMovieId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    mMovie = movie;
                    complete(mMovie);
                }
            }
        });
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void checkIfBookmark(int movieId) {
        detailViewModel.getFav(movieId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    ivBookmarks.setImageDrawable(getDrawable(R.drawable.ic_bookmark_true));
                } else {
                    ivBookmarks.setImageDrawable(getDrawable(R.drawable.ic_bookmark_false));
                }
            }
        });
    }

    private void posterImageTransition() {
        Picasso.with(this)
                .load(POSTER_BASE_URL + mMoviePoster)
                .into(vPosterImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovie != null)
            outState.putParcelable(KEY_MOVIE_PERSISTENCE, Parcels.wrap(mMovie));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupReviews(List<Reviews> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            ReviewsAdapter vReviewsAdapter = new ReviewsAdapter(reviews);
            rvReviews.setHasFixedSize(true);
            rvReviews.setLayoutManager(new LinearLayoutManager(this));
            rvReviews.setAdapter(vReviewsAdapter);
        } else {
            rvReviews.setVisibility(View.GONE);
            tvNoReview.setVisibility(View.VISIBLE);
        }
    }

    private void setupTrailers(List<Trailers> trailers) {
        if (trailers != null && !trailers.isEmpty()) {
            TrailersAdapter vTrailersAdapter = new TrailersAdapter(trailers, new IWatchTrailerClickHandler() {
                @Override
                public void onClick(Trailers trailers) {
                    TrailerIntentHandler.viewTrailer(trailers, MovieDetailActivity.this);
                }
            }, new IShareTrailerHandler() {
                @Override
                public void onClick(Trailers trailers) {
                    TrailerIntentHandler.shareTrailerLink(trailers, MovieDetailActivity.this);
                }
            });
            rvTrailers.setHasFixedSize(true);
            rvTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvTrailers.setAdapter(vTrailersAdapter);
        } else {
            rvTrailers.setVisibility(View.GONE);
            tvNoTrailer.setVisibility(View.VISIBLE);
        }
    }

    public void setStatusBarColorFromBackdrop(final String url) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap sBitmap = getBitmapFromUrl(url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int backGroundColor = ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark);
                        if (getDarkVibrantColor(sBitmap) != null) {
                            backGroundColor = getDarkVibrantColor(sBitmap).getRgb();
                        }
                        getWindow().setStatusBarColor(backGroundColor);
                        vCollapsingToolbarLayout.setContentScrimColor(backGroundColor);
                    }
                });
            }
        });
    }

    private static final String TAG = "MovieDetailActivity";

    @Override
    public void complete(final Movie movie) {
        Log.d(TAG, "complete: " + movie.toString());
        setStatusBarColorFromBackdrop(BACKDROP_BASE_URL + movie.getBackdrop());
        Picasso.with(MovieDetailActivity.this)
                .load(BACKDROP_BASE_URL + movie.getBackdrop())
                .into(mBackdropImageView);
        mMoviePoster = movie.getMoviePoster();
        Log.d(TAG, "complete: " + movie.getMoviePoster());
        posterImageTransition();
        vCollapsingToolbarLayout.setTitleEnabled(false);
        tvMovieTitle.setText(movie.getMovieTitle());
        rbMovieRating.setRating(movie.getVoterAverage() / 2);
        tvReleaseDate.setText(getApplication().getResources().getString(R.string.label_release_date).concat(movie.getMovieReleaseDate()));
        tvOverview.setText(movie.getMovieOverview());
        setupTrailers(movie.getTrailers());
        setupReviews(movie.getReviews());
        mMovie = movie;
        ivBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailViewModel.bookmarkMovie(movie);
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
