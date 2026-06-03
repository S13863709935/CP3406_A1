package com.example.weatherforecastd9k.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.weatherforecastd9k.db.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    User findByPhone(String phone);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findByUsername(String username);

    @Query("SELECT * FROM users LIMIT 1")
    User getUser();

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username AND password = :password)")
    boolean verifyPassword(String username, String password);

    @Query("UPDATE users SET password = :newPassword WHERE username = :username")
    void updatePassword(String username, String newPassword);
} 