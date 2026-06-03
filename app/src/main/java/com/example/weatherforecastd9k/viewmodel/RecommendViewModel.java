package com.example.weatherforecastd9k.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherforecastd9k.repository.RecommendRepository;

public class RecommendViewModel extends AndroidViewModel {
    private final RecommendRepository repository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public RecommendViewModel(@NonNull Application application) {
        super(application);
        repository = new RecommendRepository(application);
    }

    public LiveData<Bitmap> getBingImage() {
        isLoading.setValue(true);
        LiveData<Bitmap> result = repository.getBingImage();
        result.observeForever(bitmap -> {
            isLoading.setValue(false);
            if (bitmap == null) {
                errorMessage.setValue("Failed to load image");
            }
        });
        return result;
    }

    public LiveData<Bitmap> generateQrCode(String content) {
        if (content.isEmpty()) {
            errorMessage.setValue("Please enter content");
            return null;
        }

        LiveData<Bitmap> result = repository.generateQrCode(content);
        result.observeForever(bitmap -> {
            if (bitmap == null) {
                errorMessage.setValue("Failed to generate QR code");
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