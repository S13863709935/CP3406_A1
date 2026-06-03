package com.example.weatherforecastd9k.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.preference.PreferenceManager;
import com.example.weatherforecastd9k.db.WeatherDbHelper;
import java.util.ArrayList;
import java.util.List;

public class HistoryCityManager {
    private WeatherDbHelper dbHelper;
    private Context context;

    public static class HistoryCity {
        public String cityName;
        public String cityCode;
        public long timestamp;

        public HistoryCity(String cityName, String cityCode, long timestamp) {
            this.cityName = cityName;
            this.cityCode = cityCode;
            this.timestamp = timestamp;
        }
    }

    public HistoryCityManager(Context context) {
        this.context = context;
        this.dbHelper = new WeatherDbHelper(context);
    }

    public void addCity(String cityName, String cityCode) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // Check if it already exists
        Cursor cursor = db.query(WeatherDbHelper.TABLE_HISTORY,
                new String[]{WeatherDbHelper.COLUMN_ID},
                WeatherDbHelper.COLUMN_CITY_NAME + "=?",
                new String[]{cityName}, null, null, null);
                
        if (cursor.moveToFirst()) {
            // Already exists, update timestamp
            ContentValues values = new ContentValues();
            values.put(WeatherDbHelper.COLUMN_TIMESTAMP, System.currentTimeMillis());
            db.update(WeatherDbHelper.TABLE_HISTORY, values,
                    WeatherDbHelper.COLUMN_CITY_NAME + "=?",
                    new String[]{cityName});
        } else {
            // Does not exist, add a new record
            ContentValues values = new ContentValues();
            values.put(WeatherDbHelper.COLUMN_CITY_NAME, cityName);
            values.put(WeatherDbHelper.COLUMN_CITY_CODE, cityCode);
            values.put(WeatherDbHelper.COLUMN_TIMESTAMP, System.currentTimeMillis());
            db.insert(WeatherDbHelper.TABLE_HISTORY, null, values);
            
            // Check if the limit has been exceeded
            int maxHistory = getMaxHistorySize();
            cursor = db.query(WeatherDbHelper.TABLE_HISTORY,
                    new String[]{WeatherDbHelper.COLUMN_ID},
                    null, null, null, null,
                    WeatherDbHelper.COLUMN_TIMESTAMP + " DESC");
                    
            if (cursor.getCount() > maxHistory) {
                // Delete the oldest record
                cursor.moveToPosition(maxHistory);
                int idIndex = cursor.getColumnIndex(WeatherDbHelper.COLUMN_ID);
                if (idIndex != -1) {
                    long oldestId = cursor.getLong(idIndex);
                    db.delete(WeatherDbHelper.TABLE_HISTORY,
                            WeatherDbHelper.COLUMN_ID + "<=?",
                            new String[]{String.valueOf(oldestId)});
                }
            }
        }
        cursor.close();
    }

    public List<HistoryCity> getHistoryCities() {
        List<HistoryCity> cities = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query(WeatherDbHelper.TABLE_HISTORY,
                null, null, null, null, null,
                WeatherDbHelper.COLUMN_TIMESTAMP + " DESC");
                
        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(WeatherDbHelper.COLUMN_CITY_NAME);
            int codeIndex = cursor.getColumnIndex(WeatherDbHelper.COLUMN_CITY_CODE);
            int timeIndex = cursor.getColumnIndex(WeatherDbHelper.COLUMN_TIMESTAMP);
            
            if (nameIndex != -1 && codeIndex != -1 && timeIndex != -1) {
                cities.add(new HistoryCity(
                    cursor.getString(nameIndex),
                    cursor.getString(codeIndex),
                    cursor.getLong(timeIndex)
                ));
            }
        }
        cursor.close();
        return cities;
    }

    private int getMaxHistorySize() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("max_history_size", 5);
    }
}