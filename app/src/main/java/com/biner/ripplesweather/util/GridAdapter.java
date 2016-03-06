package com.biner.ripplesweather.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biner.ripplesweather.R;
import com.biner.ripplesweather.model.FutureWeather;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Function
 * <p/>
 * Created by Biner on 2016/2/24.
 */
public class GridAdapter extends BaseAdapter {
    List<FutureWeather> data=new ArrayList<>();
    LayoutInflater inflater;
    public GridAdapter(Context context,List<FutureWeather> data){
        super();
        this.data=data;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public FutureWeather getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.gridviewitem,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.tvAdapterTmp=(TextView)convertView.findViewById(R.id.tvAdapterTmp);
            viewHolder.ivAdaptercode_d= (ImageView) convertView.findViewById(R.id.ivAdapterCode_d);
            viewHolder.ivAdaptercode_n= (ImageView) convertView.findViewById(R.id.ivAdapterCode_n);
            viewHolder.tvAdapterWind= (TextView) convertView.findViewById(R.id.tvAdapterWind);
            viewHolder.tvAdapterDate= (TextView) convertView.findViewById(R.id.tvAdapterDate);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tvAdapterTmp.setText(getItem(position).getTmp());
        int cond_d= reflection("R.id."+getItem(position).getCond_d());
        viewHolder.ivAdaptercode_d.setImageResource(cond_d);
        int cond_n= reflection("R.id."+getItem(position).getCond_n());
        viewHolder.ivAdaptercode_n.setImageResource(cond_n);

        viewHolder.tvAdapterWind.setText(getItem(position).getWind());
        viewHolder.tvAdapterDate.setText(getItem(position).getData());

        return convertView;
    }
    private int reflection(String type){
        int i = 0;
        try {
            Field field =R.drawable.class.getField(type);
            i=field.getInt(new R.drawable());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
    static class ViewHolder{
        TextView tvAdapterTmp;
        ImageView ivAdaptercode_d;
        ImageView ivAdaptercode_n;
        TextView tvAdapterWind;
        TextView tvAdapterDate;
    }
}
