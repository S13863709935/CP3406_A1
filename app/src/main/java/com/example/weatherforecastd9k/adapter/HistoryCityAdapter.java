package com.example.weatherforecastd9k.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherforecastd9k.R;
import com.example.weatherforecastd9k.util.HistoryCityManager.HistoryCity;
import java.util.List;

public class HistoryCityAdapter extends RecyclerView.Adapter<HistoryCityAdapter.ViewHolder> {
    private List<HistoryCity> cities;
    private OnCityClickListener listener;

    public interface OnCityClickListener {
        void onCityClick(HistoryCity city);
    }

    public HistoryCityAdapter(List<HistoryCity> cities, OnCityClickListener listener) {
        this.cities = cities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryCity city = cities.get(position);
        holder.cityName.setText(city.cityName);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCityClick(city);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities != null ? cities.size() : 0;
    }

    public void updateData(List<HistoryCity> newCities) {
        this.cities = newCities;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;

        ViewHolder(View view) {
            super(view);
            cityName = view.findViewById(R.id.cityName);
        }
    }
}