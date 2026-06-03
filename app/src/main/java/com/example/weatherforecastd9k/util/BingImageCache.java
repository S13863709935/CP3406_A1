package com.example.weatherforecastd9k.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BingImageCache {
    private static final String TAG = "BingImageCache";
    private static final String CACHE_DIR = "bing_images";
    private final Context context;

    public BingImageCache(Context context) {
        this.context = context;
        createCacheDir();
    }

    private void createCacheDir() {
        File cacheDir = new File(context.getCacheDir(), CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    public File getCachedImageFile() {
        String today = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        return new File(new File(context.getCacheDir(), CACHE_DIR), "bing_" + today + ".jpg");
    }

    public boolean isCacheValid() {
        File imageFile = getCachedImageFile();
        if (!imageFile.exists()) return false;

        // Check if it is today's cache
        String today = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String fileName = imageFile.getName();
        return fileName.contains(today);
    }

    public void saveImage(byte[] imageData) {
        File imageFile = getCachedImageFile();
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            fos.write(imageData);
        } catch (IOException e) {
            Log.e(TAG, "Error saving image to cache", e);
        }
    }

    public Bitmap loadImage() {
        File imageFile = getCachedImageFile();
        if (imageFile.exists()) {
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }
        return null;
    }
} 