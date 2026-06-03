package com.example.weatherforecastd9k.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DistrictApi {
    @GET("v3/config/district")
    Call<DistrictResponse> getDistrict(
        @Query("key") String key,
        @Query("keywords") String keywords,
        @Query("subdistrict") int subdistrict,
        @Query("extensions") String extensions
    );
} 