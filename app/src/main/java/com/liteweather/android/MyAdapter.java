package com.liteweather.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kizen on 2017/6/11.
 */

public class MyAdapter extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public MyAdapter(Context context,List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater= LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应arealist_item.xml中的控件
     * @author Administrator
     */
    public final class Component{
        public TextView cityName;
        public Button deleteButtno;
        public TextView degreeTxt;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    protected void refresh(List<Map<String, Object>> list) {
        data = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Component component=null;
        if(convertView==null){
            component=new Component();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.arealist_item, null);
            component.cityName=(TextView)convertView.findViewById(R.id.arealist_item_area_name);
            component.deleteButtno=(Button)convertView.findViewById(R.id.arealist_item_delete_btn);
            component.degreeTxt=(TextView)convertView.findViewById(R.id.arealist_item_degree_text);
            convertView.setTag(component);
        }else{
            component=(Component)convertView.getTag();
        }
        //绑定数据
        component.cityName.setText((String)data.get(position).get("cityname"));
        component.degreeTxt.setText((String)data.get(position).get("degree"));
        return convertView;
    }
}
