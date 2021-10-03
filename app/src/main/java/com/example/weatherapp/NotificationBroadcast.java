package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationBroadcast extends BroadcastReceiver {

    long date;
    double minTemp, maxTemp;
    String weatherText;


    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String latitude = sharedPreferences.getString("latitude","0");
        String longitude = sharedPreferences.getString("longitude","0");


        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude + "&lon=" + longitude + "&exclude=hourly,minutely&appid=[YOUR-API-ID]";

        Intent notificationIntent = new Intent(context,MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent  = PendingIntent.getActivity(context,0,notificationIntent,0);

        Log.d("check","notification");

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url
                ,
                null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(JSONObject response) {

                try {
//                API ITEMS FOR MAIN CARD
                    JSONObject obj = response.getJSONObject("current");
                    date = obj.getLong("dt");
                    JSONArray arr = obj.getJSONArray("weather");

                    JSONObject weatherObj = arr.getJSONObject(0);
                    weatherText = weatherObj.getString("description");

                    JSONArray intentArr = response.getJSONArray("daily");
                    JSONObject cardObj = intentArr.getJSONObject(0);
                    JSONObject cardTempObj = cardObj.getJSONObject("temp");
                    minTemp = cardTempObj.getDouble("min");
                    maxTemp = cardTempObj.getDouble("max");

                    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter();

                    String day = recyclerViewAdapter.weekDay(date);
                    String minimum = recyclerViewAdapter.toCelsius((float) minTemp);
                    String maximum = recyclerViewAdapter.toCelsius((float)maxTemp);
                    String weather = weatherText;

                    String content = weather+" "+minimum+" | "+ maximum;


                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"channel");
                    builder.setSmallIcon(R.drawable.main_icon);
                    builder.setContentTitle(day);
                    builder.setContentText(content);
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

                    notificationManagerCompat.notify(1,builder.build());
                } catch (JSONException e) {
                    Log.d("check", "Error Printing data");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("check", "Error retrieving data");
            }
        });





        requestQueue.add(jsonObjectRequest);


    }
}
