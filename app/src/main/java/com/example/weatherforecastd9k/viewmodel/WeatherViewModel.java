package com.example.weatherforecastd9k.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.example.weatherforecastd9k.network.WeatherResponse;
import com.example.weatherforecastd9k.repository.WeatherRepository;

public class WeatherViewModel extends AndroidViewModel {
    private final WeatherRepository repository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final SharedPreferences prefs;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        repository = new WeatherRepository();
        prefs = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public LiveData<WeatherResponse> getWeatherData() {
        String cityCode = prefs.getString("city_code", "");
        if (cityCode.isEmpty()) {
            errorMessage.setValue("Please select a city first");
            return null;
        }

        isLoading.setValue(true);
        LiveData<WeatherResponse> result = repository.getWeather(cityCode);
        result.observeForever(response -> {
            isLoading.setValue(false);
            if (response == null) {
                errorMessage.setValue("Failed to get weather data");
            }
        });
        return result;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
} 