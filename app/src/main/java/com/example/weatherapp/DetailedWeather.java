package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailedWeather extends AppCompatActivity {

//    FOR THE DETAILED WEATHER SCREEN

    TextView windTV, humidityTV, sunriseTV, sunsetTV, minTempTv, maxTempTV, uvTV, pressureTV, mainWeatherTv, detailDayTV;
    TextView locationTextView;
    ImageView icon;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather);


        Intent intent = getIntent();
        String wind = intent.getStringExtra("Wind") + " m/s";
        String Humidity = intent.getStringExtra("Humidity") + " %";
        String Sunrise = intent.getStringExtra("Sunrise") + " AM";
        String Sunset = intent.getStringExtra("Sunset") + " PM";
        String UV = intent.getStringExtra("UV") + " uvi";
        String minTemp = intent.getStringExtra("minTemp") + " \u2103";
        String maxTemp = intent.getStringExtra("maxTemp") + " \u2103";
        String pressure = intent.getStringExtra("pressure") + " hPa";
        String weather = intent.getStringExtra("weather");
        String dayText = intent.getStringExtra("dayText");
        String location = intent.getStringExtra("location");

        int iconId = intent.getIntExtra("icon", 801);

        locationTextView = findViewById(R.id.detailLocText);
        locationTextView.setText(location);

        icon = findViewById(R.id.detailWeatherIcon);

        if (iconId >= 200 && iconId < 300) {
            icon.setImageResource(R.drawable.pic200);
        } else if (iconId >= 300 && iconId < 400) {
            icon.setImageResource(R.drawable.pic300);
        } else if (iconId == 511) {
            icon.setImageResource(R.drawable.pic511);
        } else if (iconId >= 500 && iconId < 600) {
            icon.setImageResource(R.drawable.pic500);
        } else if (iconId >= 600 && iconId < 700) {
            icon.setImageResource(R.drawable.pic600);
        } else if (iconId >= 700 && iconId < 800) {
            icon.setImageResource(R.drawable.pic700);
        } else if (iconId == 800) {
            icon.setImageResource(R.drawable.pic800);
        } else {
            icon.setImageResource(R.drawable.pic801);
        }


        windTV = findViewById(R.id.detailWindReading);
        windTV.setText(wind);

        humidityTV = findViewById(R.id.detailHumidityReading);
        humidityTV.setText(Humidity);

        sunriseTV = findViewById(R.id.detailSunRiseReadings);
        sunriseTV.setText(Sunrise);

        sunsetTV = findViewById(R.id.detailSunSetReading);
        sunsetTV.setText(Sunset);

        uvTV = findViewById(R.id.detailUVReading);
        uvTV.setText(UV);

        minTempTv = findViewById(R.id.detailMinTempReading);
        minTempTv.setText(minTemp);

        maxTempTV = findViewById(R.id.detailMaxTempReading);
        maxTempTV.setText(maxTemp);

        pressureTV = findViewById(R.id.detailPressureReading);
        pressureTV.setText(pressure);

        mainWeatherTv = findViewById(R.id.detailWeatherText);
        mainWeatherTv.setText(weather);

        detailDayTV = findViewById(R.id.detailMainText);
        detailDayTV.setText(dayText);

        progressBar = findViewById(R.id.detailProgressBar);
        progressBar.setVisibility(View.GONE);


    }
}