package com.example.weatherforecastd9k.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("v3/weather/weatherInfo")
    Call<WeatherResponse> getWeather(
        @Query("key") String key,
        @Query("city") String cityCode,
        @Query("extensions") String extensions  // base:returns live weather all:returns forecast weather
    );
} 