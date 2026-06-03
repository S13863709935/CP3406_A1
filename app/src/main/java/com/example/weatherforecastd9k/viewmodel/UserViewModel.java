package com.example.weatherforecastd9k.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherforecastd9k.db.entity.User;
import com.example.weatherforecastd9k.repository.UserRepository;

import java.util.Random;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;
    private final MutableLiveData<String> verificationCode = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public LiveData<User> login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Username and password cannot be empty");
            return null;
        }
        return repository.login(username, password);
    }

    public void sendVerificationCode(String phone) {
        if (phone.isEmpty() || phone.length() != 11) {
            errorMessage.setValue("Please enter a valid phone number");
            return;
        }

        repository.checkPhone(phone).observeForever(isAvailable -> {
            if (isAvailable) {
                // Generate a 6-digit random verification code
                String code = String.format("%06d", new Random().nextInt(999999));
                verificationCode.setValue(code);
                errorMessage.setValue("Verification code: " + code);
            } else {
                errorMessage.setValue("This phone number has already been registered");
            }
        });
    }

    public LiveData<Boolean> register(String username, String password, String phone, String code) {
        if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || code.isEmpty()) {
            errorMessage.setValue("Please fill in all fields");
            return null;
        }

        if (!code.equals(verificationCode.getValue())) {
            errorMessage.setValue("Incorrect verification code");
            return null;
        }

        return repository.register(username, password, phone);
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getVerificationCode() {
        return verificationCode;
    }
} 