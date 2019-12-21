package com.example.weatherapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeatherWeekAdapter extends RecyclerView.Adapter<WeatherWeekAdapter.WeatherViewHolder> {
    private List<WeatherDay> weatherDay;
    private final Typeface typeface;

    public WeatherWeekAdapter (List<WeatherDay> weather, Context context) {
        weatherDay=weather;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/weather_icons.ttf");
        notifyDataSetChanged();
    }

    public void clearItems() {
        weatherDay.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_day_weather, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherDay weather=weatherDay.get(position);
        holder.day_week.setText(weather.getDay_week());
        holder.dayNum.setText(weather.getDayNum());
        holder.weatherPic.setText(weather.getWeatherPic());
        holder.tempDay.setText(weather.getTempDay());
        holder.tempNight.setText(weather.getTempNight());
    }

    @Override
    public int getItemCount() {
        return weatherDay.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView day_week;
        private TextView dayNum;
        private  TextView weatherPic;
        private  TextView tempDay;
        private TextView tempNight;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            day_week=itemView.findViewById(R.id.day_week);
            dayNum=itemView.findViewById(R.id.dayNum);
            weatherPic=itemView.findViewById(R.id.weatherPic);
            weatherPic.setTypeface(typeface);
            tempDay=itemView.findViewById(R.id.tempDay);
            tempNight=itemView.findViewById(R.id.tempNight);
        }
    }
}