package com.giant.jamie.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jamie on 2017/12/28.
 */

public class QueryDataTask extends AsyncTask<String, Void, String> {

    private String TAG = QueryDataTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... strings) {

        String baseInfoResult = "";
        String weeklyForecastResult = "";

        getYahooInformation(httpsConnect(strings[1], weeklyForecastResult));

        getWUInformation(httpsConnect(strings[0], baseInfoResult));

        return null;
    }

    private String httpsConnect(String apiAddress, String result){

        URL url;
        HttpURLConnection httpURLConnection;

        try {

            url = new URL(apiAddress);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            if(httpURLConnection.getResponseCode() == 200) {

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while (data != -1) {

                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();

                }

            }

        }catch(Exception e){

            e.printStackTrace();

        }

//        Log.i(TAG, result + "_result");

        return result;

    }

    //uv index
    private void getWUInformation(String result){

        String uvIndex;
        //no connection
        if(result.isEmpty()){

            if(DataProcess.sp.getString("uv", "").isEmpty()) {

                uvIndex = "----";
//                Log.i(TAG, uvIndex + "_uvIndex");

                DataProcess.editor = DataProcess.sp.edit();
                DataProcess.editor.putString("uv", uvIndex);
                DataProcess.editor.apply();

            }

        }else {

            uvIndex = UVIndex(result);
            Log.i(TAG, uvIndex + "_uvIndex_" + result);

            DataProcess.editor = DataProcess.sp.edit();
            DataProcess.editor.putString("uv", uvIndex);
            DataProcess.editor.apply();

        }



    }

    private String UVIndex(String result){

        String uvIndex = "----";
        JSONObject json = null;

        try {

            json = new JSONObject(result);
            if(json.has("current_observation")&& json.getJSONObject("current_observation").has("UV")) {
                uvIndex = json.getJSONObject("current_observation").getString("UV");
                float uv = Integer.valueOf(uvIndex);
                if (uv <= 0f || uv < 3f) {

                    uvIndex = "Low 0.0";

                } else if (uv > 2f && uv < 6f) {

                    uvIndex = "Moderate " + uv;

                } else if (uv > 5.9f && uv < 8f) {

                    uvIndex = "High " + uv;

                } else if (uv > 7.9f && uv < 11f) {

                    uvIndex = "Very high " + uv;

                } else if (uv > 10.9f) {

                    uvIndex = "Extreme " + uv;

                }
            }else {

                uvIndex = "----";
//                Log.i(TAG, uvIndex + "_uvIndex");

                DataProcess.editor = DataProcess.sp.edit();
                DataProcess.editor.putString("uv", uvIndex);
                DataProcess.editor.apply();

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return uvIndex;

    }

    //city, local time, apparent temperature, humidity, visibility
    private void getYahooInformation(String result){

        String cityName = "----";
        String localTime = "----";
        String apparentTemperature_c = "----";
        String humidity = "----";
        String visibility = "----";
        String current_img = "not available";
        String bitmap = "";

        //no connection
        if(result.isEmpty()){

            if(DataProcess.sp.getString("city", "").isEmpty()) {

                if(!current_img.isEmpty()) {
                    String url = setURL(current_img);
                    bitmap = getBitmap(url);
                }

                DataProcess.editor = DataProcess.sp.edit();
                DataProcess.editor.putString("city", cityName);
                DataProcess.editor.putString("localTime", localTime);
                DataProcess.editor.putString("ap", apparentTemperature_c + "°");
                DataProcess.editor.putString("humidity", humidity + "%");
                DataProcess.editor.putString("visibility", visibility + " m");
                DataProcess.editor.putString("currentImg", current_img);
                DataProcess.editor.putString("bitmap", bitmap);
                DataProcess.editor.apply();

            }

        }else {

            JSONObject json = null;

            try {

                json = new JSONObject(result);
                JSONObject jsonObject = json.getJSONObject("query").getJSONObject("results").getJSONObject("channel");

                //base info
                if(jsonObject.has("location") && jsonObject.getJSONObject("location").has("city")) {

                    String city = jsonObject.getJSONObject("location").getString("city");
                    cityName = DataProcess.editCity(city);

                }

                if(jsonObject.has("lastBuildDate")) {

                    String lastBuild = jsonObject.getString("lastBuildDate");
                    if (lastBuild.length() == 29) {

                        localTime = lastBuild.substring(17, 29);
//                Log.i(TAG, lastBuild.length()+"");

                    } else {

                        localTime = lastBuild;

                    }

                }

                if(jsonObject.has("item")&& jsonObject.getJSONObject("item").has("condition")) {

                    if(jsonObject.getJSONObject("item").getJSONObject("condition").has("temp")) {

                        apparentTemperature_c = fahrenheitToCelsius(jsonObject.getJSONObject("item").getJSONObject("condition").getString("temp"));

                    }

                    if(jsonObject.getJSONObject("item").getJSONObject("condition").has("text")) {

                        current_img = jsonObject.getJSONObject("item").getJSONObject("condition").getString("text");
                        if(!current_img.isEmpty()) {
                            String url = setURL(current_img);
                            bitmap = getBitmap(url);
                        }
                    }

                }

                if(jsonObject.has("atmosphere")){

                    if(jsonObject.getJSONObject("atmosphere").has("humidity")){

                        humidity = jsonObject.getJSONObject("atmosphere").getString("humidity");

                    }

                    if(jsonObject.getJSONObject("atmosphere").has("visibility")){

                        visibility = jsonObject.getJSONObject("atmosphere").getString("visibility");

                    }

                }

                DataProcess.editor = DataProcess.sp.edit();
                DataProcess.editor.putString("city", cityName);
                DataProcess.editor.putString("localTime", localTime);
                DataProcess.editor.putString("ap", apparentTemperature_c + "°");
                DataProcess.editor.putString("humidity", humidity + "%");
                DataProcess.editor.putString("visibility", visibility + " m");
                DataProcess.editor.putString("currentImg", current_img);
                DataProcess.editor.putString("bitmap", bitmap);
                DataProcess.editor.apply();

                Log.i(TAG, cityName + "_city_" + localTime + "_localTime_" + apparentTemperature_c
                        + "_ap_" + humidity + "_humidity_" + visibility + "_visibility" + current_img + "_currentImg");

                //forecast
                JSONArray jArray = json.getJSONObject("query").getJSONObject("results").getJSONObject("channel").
                        getJSONObject("item").getJSONArray("forecast");
                eachDayWeather(jArray, 0);
                eachDayWeather(jArray, 1);
                eachDayWeather(jArray, 2);
                eachDayWeather(jArray, 3);
                eachDayWeather(jArray, 4);

            } catch (Exception e) {

                e.printStackTrace();

            }
        }

    }

    private void eachDayWeather(JSONArray array, int num){

        try {

            JSONObject dayNum = array.getJSONObject(num);
            String day_num = dayNum.getString("day");
            String high_num = fahrenheitToCelsius(dayNum.getString("high"));
            String low_num = fahrenheitToCelsius(dayNum.getString("low"));
            String text_num = dayNum.getString("text");
            String bitmap_num = "";
            if(!text_num.isEmpty()) {
                String url = setURL(text_num);
                bitmap_num = getBitmap(url);
            }

            DataProcess.editor = DataProcess.sp.edit();
            DataProcess.editor.putString("day" + num, day_num);
            DataProcess.editor.putString("high" + num, high_num + "°");
            DataProcess.editor.putString("low" + num, low_num + "°");
            DataProcess.editor.putString("text" + num, text_num);
            DataProcess.editor.putString("bitmap" + num, bitmap_num);
            DataProcess.editor.apply();

            Log.i(TAG, day_num + "_" + high_num + "_" + low_num + "_" + text_num);

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    private String fahrenheitToCelsius(String f){

        float float_f = Float.valueOf(f);
        float float_c = (float_f - 32) * 5 / 9;

        return String.valueOf((int) float_c);

    }

    private String getBitmap(String url){

        Bitmap mIcon = null;
        String encodedImage = "";

        try {

            InputStream in = new java.net.URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mIcon.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();

            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Log.i(TAG, encodedImage);
        return encodedImage;

    }

    private String setURL(String text){

        int iconNum = DataProcess.getIcon(text);
        String url = "http://l.yimg.com/a/i/us/we/52/" + iconNum + ".gif";

        return url;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        WeatherForecastActivity.updateUI();
        WeatherForecastActivity.updateUIImg();

    }

}
