package com.example.weatherforecastd9k.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LOGIN_TIME = "login_time";
    private static final long SESSION_TIMEOUT = 60 * 1000; // 1分钟

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSession(String username) {
        prefs.edit()
            .putString(KEY_USERNAME, username)
            .putLong(KEY_LOGIN_TIME, System.currentTimeMillis())
            .apply();
    }

    public boolean isSessionValid() {
        long loginTime = prefs.getLong(KEY_LOGIN_TIME, 0);
        return System.currentTimeMillis() - loginTime < SESSION_TIMEOUT;
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
} 