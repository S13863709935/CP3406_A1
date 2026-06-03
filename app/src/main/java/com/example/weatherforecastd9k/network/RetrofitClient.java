package com.example.weatherforecastd9k.network;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static Retrofit bingRetrofit;
    private static final String BASE_URL = "https://restapi.amap.com/";
    private static final String BING_BASE_URL = "https://ipgeo-bingpic.hf.space/";
    private static Retrofit qrCodeRetrofit;
    private static final String QRCODE_BASE_URL = "https://api.pwmqr.com/";
    private static String apiKey;

    public static void init(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            apiKey = bundle.getString("weather.api.key");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getBingInstance() {
        if (bingRetrofit == null) {
            bingRetrofit = new Retrofit.Builder()
                    .baseUrl(BING_BASE_URL)
                    .build();
        }
        return bingRetrofit;
    }

    public static <T> T create(Class<T> serviceClass) {
        return getInstance().create(serviceClass);
    }

    public static <T> T createBingApi(Class<T> serviceClass) {
        return getBingInstance().create(serviceClass);
    }

    public static String getApiKey() {
        if (apiKey == null) {
            throw new IllegalStateException("API Key not initialized. Call init() first.");
        }
        return apiKey;
    }

    public static Retrofit getQrCodeInstance() {
        if (qrCodeRetrofit == null) {
            qrCodeRetrofit = new Retrofit.Builder()
                    .baseUrl(QRCODE_BASE_URL)
                    .build();
        }
        return qrCodeRetrofit;
    }

    public static <T> T createQrCodeApi(Class<T> serviceClass) {
        return getQrCodeInstance().create(serviceClass);
    }
} 