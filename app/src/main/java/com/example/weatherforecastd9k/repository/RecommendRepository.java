package com.example.weatherforecastd9k.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherforecastd9k.network.BingApi;
import com.example.weatherforecastd9k.network.QrCodeApi;
import com.example.weatherforecastd9k.network.RetrofitClient;
import com.example.weatherforecastd9k.util.BingImageCache;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendRepository {
    private final BingImageCache imageCache;
    private final BingApi bingApi;
    private final QrCodeApi qrCodeApi;

    public RecommendRepository(Context context) {
        imageCache = new BingImageCache(context);
        bingApi = RetrofitClient.createBingApi(BingApi.class);
        qrCodeApi = RetrofitClient.createQrCodeApi(QrCodeApi.class);
    }

    public LiveData<Bitmap> getBingImage() {
        MutableLiveData<Bitmap> result = new MutableLiveData<>();

        // Check cache first
        if (imageCache.isCacheValid()) {
            Bitmap cachedImage = imageCache.loadImage();
            if (cachedImage != null) {
                result.setValue(cachedImage);
                return result;
            }
        }

        // Load from network
        bingApi.getWallpaper("1920x1080", 0)
            .enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            byte[] imageData = response.body().bytes();
                            imageCache.saveImage(imageData);
                            Bitmap bitmap = imageCache.loadImage();
                            result.setValue(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                            result.setValue(null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    result.setValue(null);
                }
            });

        return result;
    }

    public LiveData<Bitmap> generateQrCode(String content) {
        MutableLiveData<Bitmap> result = new MutableLiveData<>();

        qrCodeApi.createQrCode(content)
            .enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            byte[] imageData = response.body().bytes();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                            result.setValue(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                            result.setValue(null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    result.setValue(null);
                }
            });

        return result;
    }
} 