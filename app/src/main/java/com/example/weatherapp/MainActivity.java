package com.example.weatherapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerViewAdapter recyclerViewAdapter;

    public String url;

    double latitude, longitude;

    CardView cardView;
    TextView weatherTextView, dayTextView, locationTextView;
    ImageView weatherIcon;

    ProgressBar progressBar;

    String city = null,  state = null, country = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("check","onCreate");

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;



//        Checks for permissions in Manifest File
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )&& (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Log.d("check","permission");
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }

//        Check for GPS
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.d("check","gps");
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(this)
                    .setTitle("Location Permission")
                    .setMessage("Permission denied. Turn On Permission")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
//            Getting user location
            android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
            url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude + "&lon=" + longitude + "&exclude=hourly,minutely&appid=[YOUR-API-ID]";

//            Storing the Last known location
            SharedPreferences sharedPreferences = getSharedPreferences("data",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("latitude",""+latitude);
            editor.putString("longitude",""+longitude);
            editor.apply();


            Log.d("check",sharedPreferences.getString("url"," Null"));

            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(latitude,
                        longitude, 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    country = addresses.get(0).getCountryName();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        String location = city + " - " + state + ", " + country;

        progressBar = findViewById(R.id.progressBar);


//        MAIN CARD
        cardView = findViewById(R.id.MainCard);
        weatherTextView = findViewById(R.id.mainWeatherText);
        dayTextView = findViewById(R.id.mainText);
        weatherIcon = findViewById(R.id.mainWeatherIcon);
        locationTextView = findViewById(R.id.mainLocText);



        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



//        API call
        VolleyAPI volleyAPI = new VolleyAPI(this, weatherTextView, dayTextView, recyclerView, cardView, weatherIcon, progressBar, url, location,locationTextView);
        volleyAPI.getApiList();

        createNotificationChannel();
        Intent intent = new Intent(this,NotificationBroadcast.class);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(this,0,intent,0);



//        Notification Repeat
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        long startTime = System.currentTimeMillis();

        Log.d("check:","am called");

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,startTime,3*AlarmManager.INTERVAL_HOUR,pendingIntent);

    }

//    Notification Channel
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);}
    }
}
