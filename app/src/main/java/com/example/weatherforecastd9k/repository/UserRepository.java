package com.example.weatherforecastd9k.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherforecastd9k.db.AppDatabase;
import com.example.weatherforecastd9k.db.dao.UserDao;
import com.example.weatherforecastd9k.db.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<User> login(String username, String password) {
        MutableLiveData<User> result = new MutableLiveData<>();
        executorService.execute(() -> {
            User user = userDao.login(username, password);
            result.postValue(user);
        });
        return result;
    }

    public LiveData<Boolean> register(String username, String password, String phone) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                // 检查用户名和手机号是否已存在
                if (userDao.findByUsername(username) != null) {
                    result.postValue(false);
                    return;
                }
                if (userDao.findByPhone(phone) != null) {
                    result.postValue(false);
                    return;
                }

                // 创建新用户
                User user = new User(username, password, phone);
                userDao.insert(user);
                result.postValue(true);
            } catch (Exception e) {
                result.postValue(false);
            }
        });
        return result;
    }

    public LiveData<Boolean> checkPhone(String phone) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        executorService.execute(() -> {
            User user = userDao.findByPhone(phone);
            result.postValue(user == null);
        });
        return result;
    }

    public LiveData<Boolean> changePassword(String username, String oldPassword, String newPassword) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                // 验证原密码
                if (!userDao.verifyPassword(username, oldPassword)) {
                    result.postValue(false);
                    return;
                }

                // 更新密码
                userDao.updatePassword(username, newPassword);
                result.postValue(true);
            } catch (Exception e) {
                result.postValue(false);
            }
        });
        return result;
    }
} 