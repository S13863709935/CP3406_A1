package com.example.weatherforecastd9k.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    private String status;
    private String count;
    private String info;
    private String infocode;
    private List<Forecast> forecasts;

    public static class Forecast {
        private String city;
        private String adcode;
        private String province;
        private String reporttime;
        private List<Cast> casts;

        public static class Cast {
            private String date;
            private String week;
            private String dayweather;
            private String nightweather;
            private String daytemp;
            private String nighttemp;
            private String daywind;
            private String nightwind;
            private String daypower;
            private String nightpower;
            @SerializedName("daytemp_float")
            private String daytempFloat;
            @SerializedName("nighttemp_float")
            private String nighttempFloat;

            // Getters
            public String getDate() { return date; }
            public String getWeek() { return week; }
            public String getDayweather() { return dayweather; }
            public String getNightweather() { return nightweather; }
            public String getDaytemp() { return daytemp; }
            public String getNighttemp() { return nighttemp; }
            public String getDaywind() { return daywind; }
            public String getNightwind() { return nightwind; }
            public String getDaypower() { return daypower; }
            public String getNightpower() { return nightpower; }
            public String getDaytempFloat() { return daytempFloat; }
            public String getNighttempFloat() { return nighttempFloat; }
        }

        // Getters
        public String getCity() { return city; }
        public String getAdcode() { return adcode; }
        public String getProvince() { return province; }
        public String getReporttime() { return reporttime; }
        public List<Cast> getCasts() { return casts; }
    }

    // Getters
    public String getStatus() { return status; }
    public String getCount() { return count; }
    public String getInfo() { return info; }
    public String getInfocode() { return infocode; }
    public List<Forecast> getForecasts() { return forecasts; }

    public boolean isSuccess() {
        return "1".equals(status);
    }
} 