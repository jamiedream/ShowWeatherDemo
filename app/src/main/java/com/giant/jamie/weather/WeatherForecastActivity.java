package com.giant.jamie.weather;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by G96937 on 2017/12/28.
 */

public class WeatherForecastActivity extends AppCompatActivity {

    private String TAG = WeatherForecastActivity.class.getSimpleName();
    private int permission_request_code = 0;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private long minTime = 1000;

    private static TextView location;
    private static TextView localtime;
    private static TextView day1, day2, day3, day4, day5;
    private static ImageView weather_image1, weather_image2, weather_image3, weather_image4, weather_image5;
    private static TextView ht1, ht2, ht3, ht4, ht5;
    private static TextView lt1, lt2, lt3, lt4, lt5;
    private static TextView ap, visibility, humidity, uv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_weatherforecast);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        LastData.sp = getSharedPreferences("cacheData", MODE_PRIVATE);

        //UI
        int window_W = Resources.getSystem().getDisplayMetrics().widthPixels;
        location = findViewById(R.id.location);
        localtime = findViewById(R.id.local_time);
        scaleDayForecast(window_W);
        scaleCurrentObservation(window_W);
        updateUI();

    }

    private void scaleDayForecast(int w){

        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        ht1 = findViewById(R.id.ht1);
        ht2 = findViewById(R.id.ht2);
        ht3 = findViewById(R.id.ht3);
        ht4 = findViewById(R.id.ht4);
        ht5 = findViewById(R.id.ht5);
        lt1 = findViewById(R.id.lt1);
        lt2 = findViewById(R.id.lt2);
        lt3 = findViewById(R.id.lt3);
        lt4 = findViewById(R.id.lt4);
        lt5 = findViewById(R.id.lt5);

        int day_weight = 4;
        int ht_weight = 5;
        int lt_weight = 10;

        day1.setWidth(w / day_weight);
        day2.setWidth(w / day_weight);
        day3.setWidth(w / day_weight);
        day4.setWidth(w / day_weight);
        day5.setWidth(w / day_weight);
        ht1.setWidth(w / ht_weight);
        ht2.setWidth(w / ht_weight);
        ht3.setWidth(w / ht_weight);
        ht4.setWidth(w / ht_weight);
        ht5.setWidth(w / ht_weight);
        ht1.setGravity(Gravity.END);
        ht2.setGravity(Gravity.END);
        ht3.setGravity(Gravity.END);
        ht4.setGravity(Gravity.END);
        ht5.setGravity(Gravity.END);
        lt1.setWidth(w / lt_weight);
        lt2.setWidth(w / lt_weight);
        lt3.setWidth(w / lt_weight);
        lt4.setWidth(w / lt_weight);
        lt5.setWidth(w / lt_weight);
        lt1.setGravity(Gravity.END);
        lt2.setGravity(Gravity.END);
        lt3.setGravity(Gravity.END);
        lt4.setGravity(Gravity.END);
        lt5.setGravity(Gravity.END);

    }

    private void scaleCurrentObservation(int w){

        ap = findViewById(R.id.ap);
        humidity = findViewById(R.id.humidity);
        visibility = findViewById(R.id.visibility);
        uv = findViewById(R.id.uv);

    }

    public static void updateUI(){

        location.setText(LastData.sp.getString("city", ""));
        localtime.setText(LastData.sp.getString("localTime", ""));
        day1.setText(LastData.sp.getString("day0", ""));
        day2.setText(LastData.sp.getString("day1", ""));
        day3.setText(LastData.sp.getString("day2", ""));
        day4.setText(LastData.sp.getString("day3", ""));
        day5.setText(LastData.sp.getString("day4", ""));
        ht1.setText(LastData.sp.getString("high0", ""));
        ht2.setText(LastData.sp.getString("high1", ""));
        ht3.setText(LastData.sp.getString("high2", ""));
        ht4.setText(LastData.sp.getString("high3", ""));
        ht5.setText(LastData.sp.getString("high4", ""));
        lt1.setText(LastData.sp.getString("low0", ""));
        lt2.setText(LastData.sp.getString("low1", ""));
        lt3.setText(LastData.sp.getString("low2", ""));
        lt4.setText(LastData.sp.getString("low3", ""));
        lt5.setText(LastData.sp.getString("low4", ""));
        ap.setText(LastData.sp.getString("ap", ""));
        humidity.setText(LastData.sp.getString("humidity", ""));
        visibility.setText(LastData.sp.getString("visibility", ""));
        uv.setText(LastData.sp.getString("uv", ""));

    }

    @Override
    protected void onResume() {
        super.onResume();

        getMarshmallowPermission();
        getCoordinate();

        asyncHandler.postDelayed(asyncRunnable, asyncFrequency);

    }

    private void getMarshmallowPermission() {

        //if android 6.0+ then turn on location detection
        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, permission_request_code);

            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permission_request_code);

            }

        }

    }

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(WeatherForecastActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                        permission_request_code);
                            }
                        })
                        .create()
                        .show();
            } else {

                ActivityCompat.requestPermissions(WeatherForecastActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        permission_request_code);

            }

        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(WeatherForecastActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        permission_request_code);
                            }
                        })
                        .create()
                        .show();
            } else {

                ActivityCompat.requestPermissions(WeatherForecastActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        permission_request_code);

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == permission_request_code) {

            if (grantResults.length > 0 &&
                    grantResults[0] == permission_request_code) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                    checkPermission();

                } else {

                    Log.i(TAG, "Permission denied.");

                }

            }

        }

    }

    private String countryCode;
    private String adminCity;

    private void getCoordinate() {

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                try {

                    LastData.editor = LastData.sp.edit();
                    LastData.editor.putString("lat", LastData.setString(".4f", location.getLatitude()));
                    LastData.editor.putString("lon", LastData.setString(".4f", location.getLongitude()));
                    LastData.editor.apply();

                    Geocoder geocoder = new Geocoder(getApplicationContext(), new Locale("en", "US"));
                    List<Address> addr = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    countryCode = addr.get(0).getCountryCode();
                    adminCity = addr.get(0).getAdminArea();

                    Log.i(TAG, countryCode + "_address_" + adminCity);

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minTime, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minTime, mLocationListener);

        }

    }

    private Handler asyncHandler = new Handler();
    private long asyncFrequency = 2000;
    private Runnable asyncRunnable = new Runnable() {

        @Override
        public void run() {

            if(countryCode != null && adminCity != null) {

                Log.i(TAG, "NOT NULL_" + countryCode + "__" + adminCity);
                for(char c: adminCity.toCharArray()) {

                    //check if city name is english(for wunder weather)
                    if (Character.UnicodeBlock.of(c) != Character.UnicodeBlock.BASIC_LATIN) {

                        if(adminCity.equals("台中市")){

                            adminCity = getString(R.string.taichung);

                        }else if(adminCity.equals("台北市")){

                            adminCity = getString(R.string.taipei);

                        }

//                        asyncHandler.postDelayed(this, asyncFrequency);
//                        break;

                    }else{

                        mLocationManager.removeUpdates(mLocationListener);

                        //deal with city string
                        if(adminCity.contains("CITY")){

                            int length = adminCity.length();
                            //todo
                            adminCity.substring(0, length - 5);
                            Log.i(TAG, length + "_addressCityLength");

                        }else if(adminCity.contains(" ")){

                            adminCity.replace(" ", "_");

                        }

                        Log.i(TAG, adminCity + "_city name");

                    }
                }

                asyncApi(countryCode, adminCity);

            }else{

                asyncHandler.postDelayed(this, asyncFrequency);
                Log.i(TAG, "NULL");

            }

        }

    };

    private void asyncApi(String state, String cityName){

        LastData.editor = LastData.sp.edit();
        LastData.editor.putString("city", cityName);
        LastData.editor.putString("state", state);
        LastData.editor.apply();

        String baseInfoUrl = "http://api.wunderground.com/api/" + getString(R.string.weather_underground_key) + "/conditions/q/" + state + "/" + cityName + ".json";
        String weeklyForecast = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast"
                + "%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"
                + cityName + "%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        QueryAsyncTask task = new QueryAsyncTask();
        try {

            task.execute(baseInfoUrl, weeklyForecast);

        } catch (Exception e) {

            Log.i(TAG, e.toString());

        }


    }

}
