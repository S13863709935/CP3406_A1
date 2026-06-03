package com.example.weatherforecastd9k.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherforecastd9k.network.RetrofitClient;
import com.example.weatherforecastd9k.network.WeatherApi;
import com.example.weatherforecastd9k.network.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    private final WeatherApi weatherApi;

    public WeatherRepository() {
        weatherApi = RetrofitClient.create(WeatherApi.class);
    }

    public LiveData<WeatherResponse> getWeather(String cityCode) {
        MutableLiveData<WeatherResponse> result = new MutableLiveData<>();

        weatherApi.getWeather(RetrofitClient.getApiKey(), cityCode, "all")
            .enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if (response.isSuccessful()) {
                        result.setValue(response.body());
                    } else {
                        result.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    result.setValue(null);
                }
            });

        return result;
    }
} 