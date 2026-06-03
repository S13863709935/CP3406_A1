package com.example.weatherforecastd9k.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weatherforecastd9k.repository.HistoryRepository;
import com.example.weatherforecastd9k.util.HistoryCityManager.HistoryCity;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private final HistoryRepository repository;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new HistoryRepository(application);
    }

    public LiveData<List<HistoryCity>> getHistoryCities() {
        return repository.getHistoryCities();
    }

    public void selectCity(HistoryCity city) {
        repository.selectCity(city);
    }
} 