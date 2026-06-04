package com.example.weatherforecastd9k.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecastd9k.R;
import com.example.weatherforecastd9k.adapter.HistoryCityAdapter;
import com.example.weatherforecastd9k.util.HistoryCityManager;
import com.example.weatherforecastd9k.util.HistoryCityManager.HistoryCity;

public class HistoryFragment extends Fragment implements HistoryCityAdapter.OnCityClickListener {
    private RecyclerView historyList;
    private HistoryCityAdapter adapter;
    private HistoryCityManager historyManager;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadHistoryCities();
        
        return view;
    }

    private void initViews(View view) {
        historyList = view.findViewById(R.id.historyList);
        historyManager = new HistoryCityManager(requireContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
    }

    private void setupRecyclerView() {
        historyList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new HistoryCityAdapter(null, this);
        historyList.setAdapter(adapter);
    }

    private void loadHistoryCities() {
        adapter.updateData(historyManager.getHistoryCities());
    }

    @Override
    public void onCityClick(HistoryCity city) {
        // Update current city setting
        prefs.edit()
            .putString("default_city", city.cityName)
            .putString("city_code", city.cityCode)
            .putBoolean("auto_location", false)
            .apply();

        // Show tip
        Toast.makeText(requireContext(), 
            "Switched to: " + city.cityName, Toast.LENGTH_SHORT).show();

        // Switch to weather page
        requireActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.nav_host_fragment, new WeatherFragment())
            .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list every time the page becomes visible
        loadHistoryCities();
    }
} 