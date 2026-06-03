package com.example.weatherforecastd9k.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DistrictResponse {
    private String status;
    private String info;
    private String infocode;
    private List<District> districts;

    public static class District {
        private String citycode;
        private String adcode;
        private String name;
        private String center;
        private String level;
        private List<District> districts;

        // Getters
        public String getCitycode() { return citycode; }
        public String getAdcode() { return adcode; }
        public String getName() { return name; }
        public String getCenter() { return center; }
        public String getLevel() { return level; }
        public List<District> getDistricts() { return districts; }
    }

    // Getters
    public String getStatus() { return status; }
    public String getInfo() { return info; }
    public String getInfocode() { return infocode; }
    public List<District> getDistricts() { return districts; }

    public boolean isSuccess() {
        return "1".equals(status);
    }
} 