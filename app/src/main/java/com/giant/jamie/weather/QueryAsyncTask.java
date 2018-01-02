package com.giant.jamie.weather;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by G96937 on 2017/12/28.
 */

public class QueryAsyncTask extends AsyncTask<String, Void, String> {

    private String TAG = QueryAsyncTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... strings) {

        String baseInfoResult = "";
        String weeklyForecastResult = "";

        //base info
        getBaseInformation(httpsConnect(strings[0], baseInfoResult));

        //weekly forecast
        getWeeklyForecast(httpsConnect(strings[1], weeklyForecastResult));

        return null;
    }

    private String httpsConnect(String apiAddress, String result){


        URL url;
        HttpURLConnection httpURLConnection;

        try {
            url = new URL(apiAddress);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int data = inputStreamReader.read();
            while (data != -1) {

                char current = (char) data;
                result += current;
                data = inputStreamReader.read();

            }
        }catch(Exception e){

            e.printStackTrace();

        }

        Log.i(TAG, result + "_result");

        return result;

    }

    //city, local time, apparent temperature, humidity, visibility, uv index
    private void getBaseInformation(String result){

        JSONObject json = null;

        try {

            json = new JSONObject(result);
            String city = json.getJSONObject("current_observation").getJSONObject("display_location").getString("city");
            String localTime = json.getJSONObject("current_observation").getString("observation_time");
            String timeZoneShort = json.getJSONObject("current_observation").getString("local_tz_short");

            String apparentTemperature_c = json.getJSONObject("current_observation").getString("feelslike_c");
            String humidity = json.getJSONObject("current_observation").getString("relative_humidity");
            String visibility = json.getJSONObject("current_observation").getString("visibility_mi");
            String uvIndex = json.getJSONObject("current_observation").getString("UV");

            Log.i(TAG, city + "_city_" + localTime + "_localTime_" + timeZoneShort + "_timeZoneShort_" + apparentTemperature_c
            + "_ap_" + humidity + "_humidity_" + visibility + "_visibility_" + uvIndex + "_uvIndex");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getWeeklyForecast(String result){

        JSONObject json = null;

        try{

            json = new JSONObject(result);
            JSONArray jArray = json.getJSONObject("query").getJSONObject("results").getJSONObject("channel").
                    getJSONObject("item").getJSONArray("forecast");

            //forecast
            eachDayWeather(jArray, 0);
            eachDayWeather(jArray, 1);
            eachDayWeather(jArray, 2);
            eachDayWeather(jArray, 3);
            eachDayWeather(jArray, 4);

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    private void eachDayWeather(JSONArray array, int num){

        try {

            JSONObject dayNum = array.getJSONObject(num);
            String day_num = dayNum.getString("day");
            String high_num = fahrenheitToCelsius(dayNum.getString("high"));
            String low_num = fahrenheitToCelsius(dayNum.getString("low"));
            String text_num = dayNum.getString("text");

            Log.i(TAG, day_num + "_" + high_num + "_" + low_num + "_" + text_num);


        }catch(Exception e){

            e.printStackTrace();

        }

    }

    private String fahrenheitToCelsius(String f){

        int int_f = Integer.valueOf(f);
        int int_c = (int_f - 32) * 5 / 9;

        return String.valueOf(int_c);

    }

}
