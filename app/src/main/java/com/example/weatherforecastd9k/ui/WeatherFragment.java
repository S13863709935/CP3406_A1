package com.example.weatherforecastd9k.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherforecastd9k.R;
import com.example.weatherforecastd9k.network.RetrofitClient;
import com.example.weatherforecastd9k.network.WeatherApi;
import com.example.weatherforecastd9k.network.WeatherResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * WeatherFragment, used to display weather-related information
 * Contains two sub-pages: Today's Weather and Weather Recommendations
 */
public class WeatherFragment extends Fragment {
    private static final String TAG = "WeatherFragment";
    private ViewPager2 viewPager;           // Used for swiping left and right to switch pages
    private TabLayout tabLayout;            // Top tab bar
    private WeatherPagerAdapter pagerAdapter;// Adapter for ViewPager

    /**
     * Create Fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        
        initViews(view);
        setupViewPager();
        fetchWeatherData(); // Fetch weather data
        
        return view;
    }
    
    /**
     * Initialize view components
     */
    private void initViews(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
    }
    
    /**
     * Set up ViewPager and TabLayout
     * Configure page switching and tab display
     */
    private void setupViewPager() {
        pagerAdapter = new WeatherPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        
        // Link TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText("Today");
                        break;
                    case 1:
                        tab.setText("Recommendation");
                        break;
                }
            }
        ).attach();
    }
    
    private void fetchWeatherData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String cityCode = prefs.getString("city_code", "");
        
        if (cityCode.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a city first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        RetrofitClient.create(WeatherApi.class)
            .getWeather(RetrofitClient.getApiKey(), cityCode, "all")
            .enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Use Gson to format and print JSON
                        Log.d(TAG, "Weather Response: \n" + new GsonBuilder()
                                .setPrettyPrinting()
                                .create()
                                .toJson(response.body()));
                    } else {
                        Toast.makeText(requireContext(), 
                            "Failed to fetch weather data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    Toast.makeText(requireContext(), 
                        "Network request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
} 