package com.inshorts.tmdb.assign.movie.di;


import android.app.Application;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.inshorts.tmdb.assign.movie.R;
import com.inshorts.tmdb.assign.movie.network.LoggingInterceptor;
import com.inshorts.tmdb.assign.movie.network.MovieApiServices;
import com.inshorts.tmdb.assign.movie.util.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions(){
        return RequestOptions
                .placeholderOf(R.drawable.test)
                .error(R.drawable.test);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions){
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }
    @Singleton
    @Provides
    MovieApiServices providesRetrofit(OkHttpClient.Builder okhttpclient){
        return new Retrofit.Builder()
                .baseUrl(Constants.TMDB_BASE_URL)
                .client(okhttpclient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieApiServices.class);
    }

    @Singleton
    @Provides
    OkHttpClient.Builder providesOkHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS);
    }

}

