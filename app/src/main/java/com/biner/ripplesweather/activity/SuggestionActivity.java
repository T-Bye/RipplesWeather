package com.biner.ripplesweather.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.biner.ripplesweather.R;
import com.biner.ripplesweather.model.TodayWeather;
import com.biner.ripplesweather.util.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class SuggestionActivity extends AppCompatActivity {

    private TextView tvComf;
    private TextView tvDrsg;
    private TextView tvFlu;
    private TextView tvSport;
    private TextView tvTrav;
    private TextView tvUv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
         tvComf=(TextView)findViewById(R.id.tvComf);
         tvDrsg=(TextView)findViewById(R.id.tvDrsg);
         tvFlu=(TextView)findViewById(R.id.tvFlu);
         tvSport=(TextView)findViewById(R.id.tvSport);
         tvTrav=(TextView)findViewById(R.id.tvTrav);
         tvUv=(TextView)findViewById(R.id.tvUv);
        Intent intent =getIntent();
        String cityId = intent.getStringExtra("CITYID");
        List<TodayWeather> list;
        list=DBUtil.queryTodayWeather(cityId);
        for (TodayWeather  todayWeather :list){
            tvComf.setText(todayWeather.getSuggestion_comf());
            tvDrsg.setText(todayWeather.getSuggestion_drsg());
            tvFlu.setText(todayWeather.getSuggestion_flu());
            tvSport.setText(todayWeather.getSuggestion_sport());
            tvTrav.setText(todayWeather.getSuggestion_trav());
            tvUv.setText(todayWeather.getSuggestion_uv());
        }
    }


}
