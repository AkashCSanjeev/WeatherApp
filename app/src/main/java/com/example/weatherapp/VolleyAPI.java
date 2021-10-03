package com.example.weatherapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolleyAPI {

    String url;
    ArrayList<WeatherDetails> mWeatherDetailsArrayList;
    Context context;

    long date, sunRise, sunSet;
    String pressure, humidity, wind, uv, weatherText;
    int iconId;
    double minTemp, maxTemp;

    public RecyclerView recyclerView;
    public RecyclerViewAdapter recyclerViewAdapter;

    CardView cardView;
    TextView weatherTextView, dayTextView, locationTextView;
    ImageView weatherIcon;

    ProgressBar progressBar;

    String location;


    public VolleyAPI(Context context, TextView weatherTextView, TextView dayTextView, RecyclerView recyclerView, CardView cardView, ImageView weatherIcon, ProgressBar progressBar, String url, String location, TextView locationTextView) {
        this.context = context;
        this.weatherTextView = weatherTextView;
        this.dayTextView = dayTextView;
        this.recyclerView = recyclerView;
        this.cardView = cardView;
        this.weatherIcon = weatherIcon;
        this.progressBar = progressBar;
        this.url = url;
        this.location = location;
        this.locationTextView = locationTextView;
    }

    public VolleyAPI() {

    }

    public void getApiList() {

        mWeatherDetailsArrayList = new ArrayList<>();

        Log.d("check", "volley");
        //        VOLLEY FOR API
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url
                ,
                null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("check", "onResponse");
                try {
//                API ITEMS FOR MAIN CARD
                    JSONObject obj = response.getJSONObject("current");
                    date = obj.getLong("dt");
                    JSONArray arr = obj.getJSONArray("weather");
                    sunRise = obj.getLong("sunrise");
                    sunSet = obj.getLong("sunset");
                    pressure = obj.getString("pressure");
                    humidity = obj.getString("humidity");
                    wind = obj.getString("wind_speed");
                    uv = obj.getString("uvi");

                    JSONObject weatherObj = arr.getJSONObject(0);
                    weatherText = weatherObj.getString("description");
                    iconId = weatherObj.getInt("id");

                    JSONArray intentArr = response.getJSONArray("daily");
                    JSONObject cardObj = intentArr.getJSONObject(0);
                    JSONObject cardTempObj = cardObj.getJSONObject("temp");
                    minTemp = cardTempObj.getDouble("min");
                    maxTemp = cardTempObj.getDouble("max");

                    recyclerViewAdapter = new RecyclerViewAdapter();


                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, DetailedWeather.class);
                            intent.putExtra("Wind", wind);
                            intent.putExtra("Humidity", humidity);
                            intent.putExtra("Sunrise", recyclerViewAdapter.time(sunRise));
                            intent.putExtra("Sunset", recyclerViewAdapter.time(sunSet));
                            intent.putExtra("UV", uv);
                            intent.putExtra("minTemp", recyclerViewAdapter.toCelsius((float) minTemp));
                            intent.putExtra("maxTemp", recyclerViewAdapter.toCelsius((float) maxTemp));
                            intent.putExtra("pressure", pressure);
                            intent.putExtra("weather", weatherText);
                            intent.putExtra("dayText", recyclerViewAdapter.weekDay(date));
                            intent.putExtra("icon", iconId);
                            intent.putExtra("location", location);
                            context.startActivity(intent);

                        }
                    });


                    weatherTextView.setText(weatherText);
                    dayTextView.setText(recyclerViewAdapter.weekDay(date));
                    locationTextView.setText(location);


                    if (iconId >= 200 && iconId < 300) {
                        weatherIcon.setImageResource(R.drawable.pic200);
                    } else if (iconId >= 300 && iconId < 400) {
                        weatherIcon.setImageResource(R.drawable.pic300);
                    } else if (iconId == 511) {
                        weatherIcon.setImageResource(R.drawable.pic511);
                    } else if (iconId >= 500 && iconId < 600) {
                        weatherIcon.setImageResource(R.drawable.pic500);
                    } else if (iconId >= 600 && iconId < 700) {
                        weatherIcon.setImageResource(R.drawable.pic600);
                    } else if (iconId >= 700 && iconId < 800) {
                        weatherIcon.setImageResource(R.drawable.pic700);
                    } else if (iconId == 800) {
                        weatherIcon.setImageResource(R.drawable.pic800);
                    } else {
                        weatherIcon.setImageResource(R.drawable.pic801);
                    }

//                   API ITEMS FOR DETAILS
                    for (int i = 1; i < intentArr.length(); i++) {
                        Log.d("Check", "in for loop");

                        JSONObject intentObj = intentArr.getJSONObject(i);
                        long detailDate = intentObj.getLong("dt");
                        long detailSunRise = intentObj.getLong("sunrise");
                        long detailSunSet = intentObj.getLong("sunset");
                        String detailPressure = intentObj.getString("pressure");
                        String detailHumidity = intentObj.getString("humidity");
                        String detailWind = intentObj.getString("wind_speed");
                        String detailUv = intentObj.getString("uvi");

                        JSONObject tempObj = intentObj.getJSONObject("temp");
                        double detailMinTemp = tempObj.getDouble("min");
                        double detailMaxTemp = tempObj.getDouble("max");

                        JSONArray weatherArr = intentObj.getJSONArray("weather");
                        JSONObject weather = weatherArr.getJSONObject(0);
                        String detailWeatherText = weather.getString("description");
                        int detailIconId = weather.getInt("id");

                        mWeatherDetailsArrayList.add(new WeatherDetails(detailDate, detailWind, detailMinTemp, detailMaxTemp, detailHumidity, detailSunRise, detailSunSet, detailUv, detailPressure, detailWeatherText, detailIconId, location));


                    }

                } catch (JSONException e) {
                    Log.d("check", "Error Printing data");
                    e.printStackTrace();
                }

                recyclerViewAdapter = new RecyclerViewAdapter(context, mWeatherDetailsArrayList);
                recyclerView.setAdapter(recyclerViewAdapter);

                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("check", "Error Printing data");
            }
        });
        requestQueue.add(jsonObjectRequest);

        Log.d("check", "API");

    }


}
