package com.liteweather.android.gson;

/**
 * Created by kizen on 2017/5/1.
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
