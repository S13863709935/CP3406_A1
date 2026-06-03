package com.example.weatherforecastd9k.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherforecastd9k.R;
import com.example.weatherforecastd9k.adapter.WeatherDetailsAdapter;
import com.example.weatherforecastd9k.adapter.FutureWeatherAdapter;
import com.example.weatherforecastd9k.network.WeatherResponse;
import com.example.weatherforecastd9k.util.WeatherUtil;
import com.example.weatherforecastd9k.viewmodel.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

public class TodayWeatherFragment extends Fragment {
    private SwipeRefreshLayout swipeRefresh;
    private TextView cityName, weatherDesc, temperature;
    private RecyclerView todayDetailsList;
    private RecyclerView futureWeatherList;
    private WeatherDetailsAdapter todayAdapter;
    private FutureWeatherAdapter futureAdapter;
    private View rootView;
    private WeatherViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_today_weather, container, false);
        
        initViews(rootView);
        setupViewModel();
        setupRecyclerView();
        
        return rootView;
    }

    private void initViews(View view) {
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        cityName = view.findViewById(R.id.cityName);
        weatherDesc = view.findViewById(R.id.weatherDesc);
        temperature = view.findViewById(R.id.temperature);
        todayDetailsList = view.findViewById(R.id.todayDetailsList);
        futureWeatherList = view.findViewById(R.id.futureWeatherList);
    }

    private void setupRecyclerView() {
        // Today's details
        todayDetailsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        todayAdapter = new WeatherDetailsAdapter(null, "");
        todayDetailsList.setAdapter(todayAdapter);

        // Future weather
        futureWeatherList.setLayoutManager(new LinearLayoutManager(requireContext()));
        futureAdapter = new FutureWeatherAdapter(requireContext(), null, "");
        futureWeatherList.setAdapter(futureAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), 
            isLoading -> swipeRefresh.setRefreshing(isLoading));
            
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), 
            message -> {
                if (message != null) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }
            });

        fetchWeatherData();
    }

    private void fetchWeatherData() {
        LiveData<WeatherResponse> weatherData = viewModel.getWeatherData();
        if (weatherData != null) {
            weatherData.observe(getViewLifecycleOwner(), response -> {
                if (response != null && response.getForecasts() != null 
                    && !response.getForecasts().isEmpty()) {
                    WeatherResponse.Forecast forecast = response.getForecasts().get(0);
                    if (forecast.getCasts() != null && !forecast.getCasts().isEmpty()) {
                        updateWeatherUI(forecast);
                    }
                }
            });
        }
    }

    private void updateWeatherUI(WeatherResponse.Forecast forecast) {
        WeatherResponse.Forecast.Cast todayCast = forecast.getCasts().get(0);
        updateCurrentWeather(forecast, todayCast);
        
        List<WeatherResponse.Forecast.Cast> todayList = new ArrayList<>();
        todayList.add(todayCast);
        todayAdapter.updateData(todayList, forecast.getReporttime());
        
        futureAdapter.updateData(forecast.getCasts(), forecast.getReporttime());
    }

    private void updateCurrentWeather(WeatherResponse.Forecast forecast, WeatherResponse.Forecast.Cast todayCast) {
        cityName.setText(forecast.getCity());
        weatherDesc.setText(todayCast.getDayweather());
        temperature.setText(todayCast.getDaytemp() + "°C");
        
        // Set weather icon
        ImageView weatherIcon = rootView.findViewById(R.id.weatherIcon);
        if (weatherIcon != null) {
            weatherIcon.setImageResource(WeatherUtil.getWeatherIcon(todayCast.getDayweather()));
        }
    }
} 