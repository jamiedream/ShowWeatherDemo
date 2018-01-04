package com.giant.jamie.weather;

import android.content.SharedPreferences;

/**
 * Created by G96937 on 2018/1/2.
 */

public class DataProcess {

    static SharedPreferences sp;
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

}
