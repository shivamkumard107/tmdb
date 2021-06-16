package com.inshorts.tmdb.assign.movie.ui.main.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.inshorts.tmdb.assign.movie.R;
import com.inshorts.tmdb.assign.movie.model.IMovie;
import com.inshorts.tmdb.assign.movie.model.Movie;
import com.inshorts.tmdb.assign.movie.model.MovieNetworkLite;

import static com.inshorts.tmdb.assign.movie.util.Constants.POSTER_BASE_URL;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<MovieNetworkLite> mMovieList;
    private IMovieClickHandler mIMovieClickHandler;
    private List<Movie> bookmarkMovie;

    public MoviesAdapter(List<MovieNetworkLite> movies,
                         List<Movie> favMovies,
                         IMovieClickHandler IMovieClickHandler) {

        this.mMovieList = movies;
        mIMovieClickHandler = IMovieClickHandler;
        bookmarkMovie = favMovies;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        if (mMovieList != null)
            holder.bind(mMovieList.get(position));
        if (bookmarkMovie != null)
            holder.bind(bookmarkMovie.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMovieList != null)
            return mMovieList.size();
        else
            return bookmarkMovie.size();
    }


    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        IMovie movie;
        @BindView(R.id.iv_poster_image)
        ImageView mPosterImage;
        @BindView(R.id.tv_rating_cardlabel)
        TextView mMovieRatings;
        @BindView(R.id.tv_movie_name)
        TextView mMovieName;

        MoviesViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mMovieList != null)
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movie = mMovieList.get(getAdapterPosition());
                        mIMovieClickHandler.viewMovieDetails(movie, mPosterImage, false);
                    }
                });
            else
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movie = bookmarkMovie.get(getAdapterPosition());
                        mIMovieClickHandler.viewMovieDetails(movie, mPosterImage, true);
                    }
                });

        }

        private void bind(IMovie movie) {
            Context ctx = itemView.getContext();
            Picasso.with(ctx)
                    .load(POSTER_BASE_URL + movie.getMoviePoster())
                    .error(R.drawable.test)
                    .placeholder(R.drawable.test)
                    .into(mPosterImage);
            String rating = " " + movie.getVoterAverage() + " ";
            mMovieRatings.setText(rating);
            mMovieName.setText(movie.getMovieTitle());
        }
    }

    public interface IMovieClickHandler {
        void viewMovieDetails(IMovie movie, ImageView view, boolean isBookmark);
    }

}
