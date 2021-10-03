package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<WeatherDetails> mWeatherDetailsList;

    public RecyclerViewAdapter() {

    }

    public RecyclerViewAdapter(Context context, List<WeatherDetails> weatherDetailsList) {
        Log.d("check:", "constructor");
        this.context = context;
        this.mWeatherDetailsList = weatherDetailsList;
    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        Log.d("check:", "recyclerOnCreate");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        WeatherDetails weatherDetails = mWeatherDetailsList.get(position);

        int iconId = weatherDetails.getIconID();

        holder.day.setText(weekDay(weatherDetails.getUnixDay()));
        holder.weather.setText(weatherDetails.getMainWeather());
        if (iconId >= 200 && iconId < 300) {
            holder.weatherIcon.setImageResource(R.drawable.pic200);
        } else if (iconId >= 300 && iconId < 400) {
            holder.weatherIcon.setImageResource(R.drawable.pic300);
        } else if (iconId == 511) {
            holder.weatherIcon.setImageResource(R.drawable.pic511);
        } else if (iconId >= 500 && iconId < 600) {
            holder.weatherIcon.setImageResource(R.drawable.pic500);
        } else if (iconId >= 600 && iconId < 700) {
            holder.weatherIcon.setImageResource(R.drawable.pic600);
        } else if (iconId >= 700 && iconId < 800) {
            holder.weatherIcon.setImageResource(R.drawable.pic700);
        } else if (iconId == 800) {
            holder.weatherIcon.setImageResource(R.drawable.pic800);
        } else {
            holder.weatherIcon.setImageResource(R.drawable.pic801);
        }

        Log.d("check:", "recycler");
        Log.d("check:", "recycler" + weekDay(weatherDetails.getUnixDay()));
        Log.d("check:", "recycler" + weatherDetails.getUnixDay());
    }

    @Override
    public int getItemCount() {
        return mWeatherDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView day;
        public TextView weather;
        public ImageView weatherIcon;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Log.d("check:", "viewHolder");
            Log.d("check:", "viewHolder");
            day = itemView.findViewById(R.id.recyclerText);
            weather = itemView.findViewById(R.id.recyclerWeather);
            weatherIcon = itemView.findViewById(R.id.recyclerIcon);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            WeatherDetails weatherDetails = mWeatherDetailsList.get(position);

            Intent intent = new Intent(context, DetailedWeather.class);

            intent.putExtra("Wind", weatherDetails.getWindText());
            intent.putExtra("Humidity", weatherDetails.getHumidityText());
            intent.putExtra("Sunrise", time(weatherDetails.getSunRiseText()));
            intent.putExtra("Sunset", time(weatherDetails.getSunSetText()));
            intent.putExtra("UV", weatherDetails.getUvText());
            intent.putExtra("minTemp", toCelsius((float) weatherDetails.getMinTempText()));
            intent.putExtra("maxTemp", toCelsius((float) weatherDetails.getMaxTempText()));
            intent.putExtra("pressure", weatherDetails.getPressureText());
            intent.putExtra("weather", weatherDetails.getMainWeather());
            intent.putExtra("dayText", weekDay(weatherDetails.getUnixDay()));
            intent.putExtra("icon", weatherDetails.getIconID());
            intent.putExtra("location", weatherDetails.getMainLoc());
            Log.d("text", " " + weatherDetails.getSunRiseText());
            Log.d("\ntext", " " + weatherDetails.getSunSetText());
            context.startActivity(intent);

        }


    }


//    Conversions
    public String weekDay(Long unixTime) {
        String weekday = String.format(Locale.ENGLISH, "%tA", unixTime * 1000L);
        return weekday;
    }

    public String time(Long unixTime) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String time = df.format(new Date(unixTime * 1000L));
        return time;
    }

    public String toCelsius(float kelvin) {
        float celsius = kelvin - 273.15f;

        return String.format("%.02f", celsius);
    }
}
