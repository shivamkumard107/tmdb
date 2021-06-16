package com.inshorts.tmdb.assign.movie.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.palette.graphics.Palette;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PaletteExtractorUtil {
    private static final String TAG = "PaletteExtractorUtil";

    private static Palette createPalette(Bitmap bitmap) {
        if (bitmap != null)
            return Palette
                    .from(bitmap)
                    .generate();
        else return null;
    }

    public static Palette.Swatch getDarkVibrantColor(Bitmap bitmap) {
        Palette palette = createPalette(bitmap);
        if (palette != null)
            return palette.getDarkVibrantSwatch();
        else return null;
    }

    public static Bitmap getBitmapFromUrl(String src) {
        try {
            URL vURL = new URL(src);
            HttpURLConnection vConnection = (HttpURLConnection) vURL.openConnection();
            vConnection.setDoInput(true);
            vConnection.connect();
            InputStream vInputStream = vConnection.getInputStream();
            return BitmapFactory.decodeStream(vInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
