package com.example.weatherforecastd9k.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherforecastd9k.R;
import com.example.weatherforecastd9k.network.WeatherResponse.Forecast.Cast;
import com.example.weatherforecastd9k.util.WeatherUtil;
import java.util.List;

public class WeatherDetailsAdapter extends RecyclerView.Adapter<WeatherDetailsAdapter.ViewHolder> {
    private List<Cast> weatherCasts;
    private String reportTime;

    public WeatherDetailsAdapter(List<Cast> weatherCasts, String reportTime) {
        this.weatherCasts = weatherCasts;
        this.reportTime = reportTime;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cast cast = weatherCasts.get(position);
        
        holder.dateText.setText(cast.getDate() + " " + WeatherUtil.convertWeekDay(cast.getWeek()));
        holder.temperature.setText(cast.getDaytemp() + "°C / " + cast.getNighttemp() + "°C");
        holder.weather.setText(cast.getDayweather());
        holder.windDirection.setText(cast.getDaywind());
        holder.windPower.setText(cast.getDaypower() + "Level");
        holder.reportTime.setText("Updated at: " + reportTime);

        // Set weather icon
        holder.weatherIcon.setImageResource(WeatherUtil.getWeatherIcon(cast.getDayweather()));
    }

    @Override
    public int getItemCount() {
        return weatherCasts != null ? weatherCasts.size() : 0;
    }

    public void updateData(List<Cast> newCasts, String newReportTime) {
        this.weatherCasts = newCasts;
        this.reportTime = newReportTime;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView temperature;
        TextView weather;
        TextView windDirection;
        TextView windPower;
        TextView reportTime;
        ImageView weatherIcon;

        ViewHolder(View view) {
            super(view);
            dateText = view.findViewById(R.id.dateText);
            temperature = view.findViewById(R.id.temperature);
            weather = view.findViewById(R.id.weather);
            windDirection = view.findViewById(R.id.windDirection);
            windPower = view.findViewById(R.id.windPower);
            reportTime = view.findViewById(R.id.reportTime);
            weatherIcon = view.findViewById(R.id.weatherIcon);
        }
    }
} 