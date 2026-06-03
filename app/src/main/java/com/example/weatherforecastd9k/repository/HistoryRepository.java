package com.example.weatherforecastd9k.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.example.weatherforecastd9k.util.HistoryCityManager;
import com.example.weatherforecastd9k.util.HistoryCityManager.HistoryCity;

import java.util.List;

public class HistoryRepository {
    private final HistoryCityManager historyManager;
    private final SharedPreferences prefs;

    public HistoryRepository(Context context) {
        historyManager = new HistoryCityManager(context);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public LiveData<List<HistoryCity>> getHistoryCities() {
        MutableLiveData<List<HistoryCity>> result = new MutableLiveData<>();
        result.setValue(historyManager.getHistoryCities());
        return result;
    }

    public void selectCity(HistoryCity city) {
        prefs.edit()
            .putString("default_city", city.cityName)
            .putString("city_code", city.cityCode)
            .putBoolean("auto_location", false)
            .apply();
    }
} 