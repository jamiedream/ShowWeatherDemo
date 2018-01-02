package com.giant.jamie.weather;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by G96937 on 2017/12/28.
 */

public class WeatherForecastActivity extends AppCompatActivity {

    private String TAG = WeatherForecastActivity.class.getSimpleName();
    private int permission_request_code = 0;
    private TextView location;
    private TextView localtime;
//    private HourlyForecast hourlyForecast;
    private TextView day1, day2, day3, day4, day5;
    private ImageView weather_image1, weather_image2, weather_image3, weather_image4, weather_image5;
    private ImageView ht1, ht2, ht3, ht4, ht5;
    private ImageView lt1, lt2, lt3, lt4, lt5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_weatherforecast);

        location = findViewById(R.id.location);
        localtime = findViewById(R.id.local_time);
//        hourlyForecast = findViewById(R.id.hourly_forecast);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getMarshmallowPermission();

        String cityName = "Taichung";
        String state = "";
        String baseInfoUrl = "http://api.wunderground.com/api/" + getString(R.string.weather_underground_key) + "/conditions/q/CA/San_Francisco.json";
        String weeklyForecast = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast"
                + "%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"
                + cityName + "%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        QueryAsyncTask task = new QueryAsyncTask();
        try {

            //Sunnyvale
            //http://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast where woeid=2306179 and u='c'
            //http://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast where woeid=2306179 and u='c'&format=json
            //http://api.wunderground.com/api/d18f24776ec61050/conditions/q/CA/San_Francisco.json
            task.execute(baseInfoUrl, weeklyForecast);

        }catch (Exception e){

            Log.i(TAG, e.toString());

        }

    }

    private void getMarshmallowPermission(){

        //if android 6.0+ then turn on location detection
        if(Build.VERSION.SDK_INT >= 23){

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

    private void checkPermission(){

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)){

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
            }else{

                ActivityCompat.requestPermissions(WeatherForecastActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        permission_request_code);

            }

        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){

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
            }else{

                ActivityCompat.requestPermissions(WeatherForecastActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        permission_request_code);

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == permission_request_code){

            if(grantResults.length > 0 &&
                    grantResults[0] == permission_request_code){

                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){

                    checkPermission();

                }else{

                    Log.i(TAG, "Permission denied.");

                }

            }

        }

    }

}
