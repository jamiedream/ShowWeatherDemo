package com.giant.jamie.weather;

import android.os.AsyncTask;
import android.util.Log;

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

        String result = "";
        URL url;
        HttpURLConnection httpURLConnection;

        try {

            url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int data = inputStreamReader.read();
            while(data != -1){

                char current = (char) data;
                result += current;
                data = inputStreamReader.read();
                Log.i(TAG, result + "_result");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }




        return null;
    }

    //city, local time, apparent temperature, humidity, visibility, un index
    private void getBaseInformation(String result){

        JSONObject json = null;

        try {

            json = new JSONObject(result);
            String city = json.getJSONObject("location").getString("country");
            String state_name = json.getJSONObject("location").getString("country");
            String localTime = json.getJSONObject("location").getString("country");
            String timeZoneShort = json.getJSONObject("location").getString("country");
            String apparentTemperature = json.getJSONObject("location").getString("country");
            String humidity = json.getJSONObject("location").getString("country");
            String visibility = json.getJSONObject("location").getString("country");
            String uvIndex = json.getJSONObject("location").getString("country");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
