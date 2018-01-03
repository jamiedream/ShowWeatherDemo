package com.giant.jamie.weather;

import android.content.SharedPreferences;

/**
 * Created by G96937 on 2018/1/2.
 */

public class LastData {

    static SharedPreferences sp;
    static SharedPreferences.Editor editor;

    public static String setString(String format, Object obj){

        return String.format(format.toLowerCase(), obj);

    }

}
