package com.example.weatherforecastd9k.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QrCodeApi {
    @GET("qrcode/create")
    Call<ResponseBody> createQrCode(
        @Query("url") String content
    );
} 