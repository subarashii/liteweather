package com.liteweather.android.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.liteweather.android.db.City;
import com.liteweather.android.db.County;
import com.liteweather.android.db.Province;
import com.liteweather.android.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kizen on 17/4/30.
 */

public class Utility {
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject =new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allprovince= new JSONArray(response);
                for(int i=0;i<allprovince.length();i++){
                    JSONObject proObject=allprovince.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(proObject.getString("name"));
                    province.setProvinceCode(proObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCityResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCity=new JSONArray(response);
                for(int i=0;i<allCity.length();i++){
                    JSONObject cityObj =allCity.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObj.getString("name"));
                    city.setCityCode(cityObj.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountyResponse(String response ,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allcounty=new JSONArray(response);
                for(int i=0;i<allcounty.length();i++){
                    JSONObject countyObject =allcounty.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

}
