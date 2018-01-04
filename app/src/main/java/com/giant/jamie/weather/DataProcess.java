package com.giant.jamie.weather;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Jamie on 2018/1/2.
 */

public class DataProcess {

    static SharedPreferences sp;
    static SharedPreferences img_sp;
    static SharedPreferences.Editor editor;

    public static String setString(String format, Object obj){

        return String.format(format.toLowerCase(), obj);

    }

    //deal with city string
    public static String editCity(String city){

        String cityName = "";
        if(city.contains("City")){

            int length = city.length();
            cityName = city.substring(0, length - 5);
//            Log.i(city, length + "_addressCityLength");

            if(cityName.contains(" ")){

                //wu name rule
                cityName = city.replace(" ", "_");

            }

        }

        return cityName;

    }

    public static Integer getIcon(String weather){

        int weather_img = 3200;

        if(weather.equalsIgnoreCase("tornado")) weather_img = 0;
        if(weather.equalsIgnoreCase("tropical storm") || weather.contains("Storm")) weather_img = 1;
        if(weather.equalsIgnoreCase("hurricane")) weather_img = 2;
        if(weather.equalsIgnoreCase("severe thunderstorms")) weather_img = 3;
        if(weather.equalsIgnoreCase("thunderstorms")) weather_img = 4;
        if(weather.equalsIgnoreCase("mixed rain and snow")) weather_img = 5;
        if(weather.equalsIgnoreCase("mixed rain and sleet")) weather_img = 6;
        if(weather.equalsIgnoreCase("mixed snow and sleet")) weather_img = 7;
        if(weather.equalsIgnoreCase("freezing drizzle")) weather_img = 8;
        if(weather.equalsIgnoreCase("drizzle")) weather_img = 9;
        if(weather.equalsIgnoreCase("freezing rain") || weather.contains("Rain")) weather_img = 10;
        if(weather.equalsIgnoreCase("showers")) weather_img = 12;
        if(weather.equalsIgnoreCase("snow flurries")) weather_img = 13;
        if(weather.equalsIgnoreCase("light snow showers")) weather_img = 14;
        if(weather.equalsIgnoreCase("blowing snow")) weather_img = 15;
        if(weather.equalsIgnoreCase("snow")) weather_img = 16;
        if(weather.equalsIgnoreCase("hail")) weather_img = 17;
        if(weather.equalsIgnoreCase("sleet")) weather_img = 18;
        if(weather.equalsIgnoreCase("dust")) weather_img = 19;
        if(weather.equalsIgnoreCase("foggy")) weather_img = 20;
        if(weather.equalsIgnoreCase("haze")) weather_img = 21;
        if(weather.equalsIgnoreCase("smoky")) weather_img = 22;
        if(weather.equalsIgnoreCase("blustery")) weather_img = 23;
        if(weather.equalsIgnoreCase("windy")) weather_img = 24;
        if(weather.equalsIgnoreCase("cold")) weather_img = 25;
        if(weather.equalsIgnoreCase("cloudy")) weather_img = 26;
        if(weather.equalsIgnoreCase("mostly cloudy (night)")) weather_img = 27;
        if(weather.equalsIgnoreCase("mostly cloudy (day)")) weather_img = 28;
        if(weather.equalsIgnoreCase("partly cloudy (night)")) weather_img = 29;
        if(weather.equalsIgnoreCase("partly cloudy (day)")) weather_img = 30;
        if(weather.equalsIgnoreCase("clear (night)")) weather_img = 31;
        if(weather.equalsIgnoreCase("fair (night)")) weather_img = 32;
        if(weather.equalsIgnoreCase("fair (day)")) weather_img = 33;
        if(weather.equalsIgnoreCase("mixed rain and hail")) weather_img = 34;
        if(weather.equalsIgnoreCase("fair (day)")) weather_img = 35;
        if(weather.equalsIgnoreCase("hot")) weather_img = 36;
        if(weather.equalsIgnoreCase("isolated thunderstorms")) weather_img = 37;
        if(weather.equalsIgnoreCase("scattered thunderstorms")) weather_img = 38;
        if(weather.equalsIgnoreCase("scattered showers")) weather_img = 39;
        if(weather.equalsIgnoreCase("heavy snow")) weather_img = 40;
        if(weather.equalsIgnoreCase("scattered snow showers")) weather_img = 41;
        if(weather.equalsIgnoreCase("isolated thunderstorms")) weather_img = 42;
        if(weather.equalsIgnoreCase("partly cloudy")) weather_img = 44;
        if(weather.equalsIgnoreCase("thundershowers")) weather_img = 45;
        if(weather.equalsIgnoreCase("snow showers")) weather_img = 46;
        if(weather.equalsIgnoreCase("isolated thundershowers")) weather_img = 47;

        Log.i("getIcon", weather_img + "");

        return weather_img;

    }

}
