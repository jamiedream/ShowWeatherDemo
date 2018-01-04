package com.giant.jamie.weather;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Jamie on 2018/01/04.
 */
//http://blog.51cto.com/quietmadman/1613348
public class HourlyForecast extends LinearLayout {

    private Context mContext;

    public HourlyForecast(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

    }

}
