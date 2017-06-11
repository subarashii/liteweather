package com.liteweather.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liteweather.android.gson.Weather;
import com.liteweather.android.util.HttpUtil;
import com.liteweather.android.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AreaListActivity extends AppCompatActivity {
    private LinearLayout areaList_LinerLayout;
    public DrawerLayout drawerLayout;
    private Button navButton;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_list);
        areaList_LinerLayout=(LinearLayout) findViewById(R.id.areaList_LinerLayout);
        listView=(ListView)findViewById(R.id.list_view);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherid_list=prefs.getString("weatherid_list",null);
        if(weatherid_list==null){
            weatherid_list=getIntent().getStringExtra("weatherid_list");
        }
       // firstEnterActivity();

        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("weatherid_list",weatherid_list);
        editor.apply();

        //requestWeather(weatherid_list);
        requestWeatherforListView(weatherid_list);

        drawerLayout=(DrawerLayout)findViewById(R.id.arealistDrawer);
        navButton=(Button)findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        editButton=(Button)findViewById(R.id.edit_button);
        saveButton=(Button)findViewById(R.id.save_button);
       // deleteButton=(Button)findViewById(R.id.delete_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.INVISIBLE);
              //  deleteButton.setVisibility(View.VISIBLE);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setVisibility(View.INVISIBLE);
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }
        });


    }
    public void requestWeatherforListView(String weatherListStr){
        final Context context=this;
        String aWeatherId[]=weatherListStr.split(",");
        Log.i("AreaListActivity",weatherListStr);
        final List< Map<String, Object>> list=new ArrayList<Map <String,Object>>();
 /*       runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map=new HashMap<String, Object>();
                map.put("cityname","北京");
                map.put("degree","30℃");
                Map<String,Object>map1=new HashMap<String, Object>();
                map1.put("cityname","shanghai");
                map1.put("degree","10℃");
                Map<String,Object>map2=new HashMap<String, Object>();
                map2.put("cityname","chongqing");
                map2.put("degree","30℃");
                list.add(map);
                list.add(map1);
                list.add(map2);
                MyAdapter adapter=new MyAdapter(context, list);
                listView.setAdapter(adapter);
                adapter.refresh(list);
            }
        });*/


        for(int i=0;i<aWeatherId.length;i++){
            String sCurrentWeatherId=aWeatherId[i];
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(AreaListActivity.this);
            if(prefs.getString(aWeatherId[i],null)==null){
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
                        if(weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AreaListActivity.this).edit();
                            editor.putString(weather.basic.weatherId,responseText);
                            editor.apply();
                            // tempWeaterId=weather.basic.weatherId;
                            //showWeatherInfo(list,weather);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getData(list,weather);
                                }
                            });
                        }else{
                            Toast.makeText(AreaListActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }else{
                showWeatherInfo(list,Utility.handleWeatherResponse(prefs.getString(aWeatherId[i],null)));
                Log.i("AreaListActivity","this is not the first time ,you use the app");
            }
        }
        MyAdapter adapter=new MyAdapter(context, list);
        listView.setAdapter(adapter);
        adapter.refresh(list);

    }
    public void requestWeather( final  String weatherListStr) {
         String aWeatherId[]=weatherListStr.split(",");

        //areaList_LinerLayout.removeAllViews();

        for(int i=0;i<aWeatherId.length;i++){
            String sCurrentWeatherId=aWeatherId[i];
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(AreaListActivity.this);
            if(prefs.getString(aWeatherId[i],null)==null){
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
                                    List< Map<String, Object>> list=new ArrayList<Map <String,Object>>();
                                    //showWeatherInfo(list,weather);
                                    getData(list,weather);
                                   // listView.setAdapter(new MyAdapter(this, list));
                                }else{
                                    Toast.makeText(AreaListActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }else{
                //showWeatherInfo(Utility.handleWeatherResponse(prefs.getString(aWeatherId[i],null)));
                Log.v("AreaListActivity","this is not the first time ,you use the app");
            }

        }


    }
    public void firstEnterActivity(){
        LinearLayout areaList_LinerLayout=(LinearLayout)findViewById(R.id.areaList_LinerLayout);
        areaList_LinerLayout.removeAllViews();
    }


    public void showWeatherInfo(List<Map<String, Object>> list, Weather weather){
/*
        String cityName=weather.basic.cityName;
        String degree=weather.now.temperature+"℃";

        View view = LayoutInflater.from(this).inflate(R.layout.arealist_item,areaList_LinerLayout,false);
        TextView areaNameTextView=(TextView)view.findViewById(R.id.arealist_item_area_name);
        TextView degreeTextView=(TextView)view.findViewById(R.id.arealist_item_degree_text);
        areaNameTextView.setText(cityName);
        degreeTextView.setText(degree);

        String weatherid=weather.basic.weatherId;
        areaList_LinerLayout.addView(view);*/
        getData(list,weather);
    }
    private void getData(List<Map<String, Object>> list ,Weather weather){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("cityname", weather.basic.cityName);
        map.put("degree", weather.now.temperature+"℃");
        list.add(map);
    }


}
