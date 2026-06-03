package com.example.weatherforecastd9k.util;

import android.content.Context;
import com.example.weatherforecastd9k.R;

public class WeatherUtil {
    public static String convertWeekDay(String week) {
        switch (week) {
            case "1": return "Monday";
            case "2": return "Tuesday";
            case "3": return "Wednesday";
            case "4": return "Thursday";
            case "5": return "Friday";
            case "6": return "Saturday";
            case "7": return "Sunday";
            default: return "Unknown";
        }
    }

    public static int getWeatherIcon(String weatherDesc) {
        if (weatherDesc == null) return R.drawable.ic_weather;
        
        if (weatherDesc.contains("Sunny")) {
            return R.drawable.ic_weather_sunny;
        } else if (weatherDesc.contains("Cloudy")) {
            return R.drawable.ic_weather_partly_cloudy;
        } else if (weatherDesc.contains("Overcast")) {
            return R.drawable.ic_weather_overcast;
        } else if (weatherDesc.contains("Rainy")) {
            if (weatherDesc.contains("Thunder")) {
                return R.drawable.ic_weather_thunder;
            }
            return R.drawable.ic_weather_heavy_rain;
        } else if (weatherDesc.contains("Snow")) {
            return R.drawable.ic_weather_snow;
        } else if (weatherDesc.contains("Foggy")) {
            return R.drawable.ic_weather_heavy_fog;
        }
        
        return R.drawable.ic_weather;
    }
} 