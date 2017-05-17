package com.liteweather.android;

import android.content.SharedPreferences;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liteweather.android.gson.Weather;
import com.liteweather.android.util.HttpUtil;
import com.liteweather.android.util.Utility;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AreaListActivity extends AppCompatActivity {
    String firstSelectCounty;
    private LinearLayout areaList_LinerLayout;
    public DrawerLayout drawerLayout;
    private Button navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_list);
        firstSelectCounty=getIntent().getStringExtra("weather_id");
        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AreaListActivity.this).edit();
        editor.putString("weatherid_list",firstSelectCounty);
        editor.apply();
        requestWeather(firstSelectCounty);
        areaList_LinerLayout=(LinearLayout) findViewById(R.id.areaList_LinerLayout);

        drawerLayout=(DrawerLayout)findViewById(R.id.arealistDrawer);
        navButton=(Button)findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    public void requestWeather( final  String weatherListStr) {
         String aWeatherId[]=weatherListStr.split(",");
/*
        FrameLayout areaList_item_FrameLayout=(FrameLayout)findViewById(R.id.areaList_item_FrameLayout);
        if(areaList_item_FrameLayout!=null){
            areaList_item_FrameLayout.removeAllViews();
        }
*/
        LinearLayout areaList_LinerLayout=(LinearLayout)findViewById(R.id.areaList_LinerLayout);
        areaList_LinerLayout.removeAllViews();

        for(int i=0;i<aWeatherId.length;i++){
            String sCurrentWeatherId=aWeatherId[i];
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + sCurrentWeatherId + "&key=f63ce5901ebc4706a02928bad0b597e4";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AreaListActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText =response.body().string();
                    Log.i("AreaListActivity",responseText);
                    final Weather weather=Utility.handleWeatherResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(weather!=null&&"ok".equals(weather.status)){
                                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AreaListActivity.this).edit();
                                editor.putString(weather.basic.weatherId,responseText);
                                editor.apply();
                                // tempWeaterId=weather.basic.weatherId;
                                  showWeatherInfo(weather);
                            }else{
                                Toast.makeText(AreaListActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }


    }
    public void showWeatherInfo(Weather weather){
       // areaListFrameLayout.removeAllViews();
        String cityName=weather.basic.cityName;
        String degree=weather.now.temperature+"℃";
        View view = LayoutInflater.from(this).inflate(R.layout.arealist_item,areaList_LinerLayout,false);
        TextView areaNameTextView=(TextView)view.findViewById(R.id.arealist_item_area_name);
        TextView degreeTextView=(TextView)view.findViewById(R.id.arealist_item_degree_text);
        areaNameTextView.setText(cityName);
        degreeTextView.setText(degree);
        areaList_LinerLayout.addView(view);


    }

}
