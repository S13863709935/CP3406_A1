package com.example.weatherforecastd9k.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BingApi {
    @GET("/")
    Call<ResponseBody> getWallpaper(
        @Query("size") String size,
        @Query("daysago") int daysAgo
    );
} 