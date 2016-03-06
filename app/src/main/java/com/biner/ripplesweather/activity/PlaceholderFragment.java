package com.biner.ripplesweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biner.ripplesweather.R;
import com.biner.ripplesweather.model.FutureWeather;
import com.biner.ripplesweather.model.TodayWeather;
import com.biner.ripplesweather.util.DBUtil;
import com.biner.ripplesweather.util.GridAdapter;
import com.biner.ripplesweather.util.HttpUtil;
import com.biner.ripplesweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Function
 * <p/>
 * Created by Biner on 2016/2/26.
 */
public class PlaceholderFragment extends Fragment {
    private GridView gvForecast;
    private TextView tvCityTitle;
    private TextView tvTmp;
    private TextView tvCond;
    private TextView tvLoc;
    private TextView tvsuggetion;
    private RelativeLayout rlselected;
    private ImageButton ibupdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_selected, container, false);
//            ImageView ivLocation = (ImageView) rootView.findViewById(R.id.ivLocation);
        tvsuggetion = (TextView) rootView.findViewById(R.id.tvsuggestion);
        rlselected = (RelativeLayout) rootView.findViewById(R.id.rlselected);
        tvCityTitle = (TextView) rootView.findViewById(R.id.tvCityName);
        tvTmp = (TextView) rootView.findViewById(R.id.tvItemTmp);
        tvCond = (TextView) rootView.findViewById(R.id.tvItemCond);
        tvLoc = (TextView) rootView.findViewById(R.id.tvLoc);
        gvForecast = (GridView) rootView.findViewById(R.id.gvForecast);
        ibupdate= (ImageButton) rootView.findViewById(R.id.ibupdate);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//            ivLocation.setImageBitmap(BitMapUtil.decodeSampledBitmapFromResource(getResources(),R.drawable.location,30,30));
        final String cityId = getArguments().getString("CITYID");
        tvsuggetion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SuggestionActivity.class);
                intent.putExtra("CITYID",cityId);
                startActivity(intent);
            }
        });
        ibupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                ibupdate.startAnimation(animation);
                new writeDataWeather().execute(cityId);
            }
        });
        Animation animation= AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        ibupdate.startAnimation(animation);
        new writeDataWeather().execute(cityId);
        viewTodayWeather(cityId);
    }

    //        public PlaceholderFragment newInstance(int sectionNumber) {
//
//        }
    private class writeDataWeather extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String address = "https://api.heweather.com/x3/weather?cityid=" + params[0] + "&key=b2ad4353d8eb4e048c438de6666f1153";
            DBUtil.deleteFutureWeather(params[0]);
            String JsonResult = HttpUtil.sendHttpRequest(address);
            return Utility.jsonWeatherResponse(JsonResult);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            viewTodayWeather(getArguments().getString("CITYID"));
        }


    }

    public void viewTodayWeather(String cityId) {
        List<TodayWeather> cityWeather = DBUtil.queryTodayWeather(cityId);
        if (cityWeather.size() > 0) {
            for (int i = 0; i < cityWeather.size(); i++) {
                if (cityWeather.get(i).getCityId().equals(cityId)) {
                    tvCityTitle.setText(cityWeather.get(i).getCityName());
                    tvTmp.setText(cityWeather.get(i).getTmp());
                    String bgcond = cityWeather.get(i).getCond();
                    tvCond.setText(bgcond);
                    setBackground(bgcond);
                    tvLoc.setText(cityWeather.get(i).getUpdate());
                    List<FutureWeather> data = DBUtil.queryFutureWeather(cityId);
                    GridAdapter adapter = new GridAdapter(getActivity(), data);
                    gvForecast.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }

    private void setBackground(String bgcond) {
        if ("多云".equals(bgcond) || "少云".equals(bgcond) || "晴间多云".equals(bgcond)) {
            rlselected.setBackgroundResource(R.drawable.bg_cloudy_day);
        } else if ("晴".equals(bgcond) || "平静".equals(bgcond)) {
            rlselected.setBackgroundResource(R.drawable.bg_fine_day);
        } else if ("薄雾".equals(bgcond) || "雾".equals(bgcond)) {
            rlselected.setBackgroundResource(R.drawable.bg_fog);
        } else if ("霾".equals(bgcond)) {
            rlselected.setBackgroundResource(R.drawable.bg_haze);
        } else if ("阴".equals(bgcond)) {
            rlselected.setBackgroundResource(R.drawable.bg_overcast);
        } else if ("微风".equals(bgcond) || "和风".equals(bgcond) || "疾风".equals(bgcond)
                || "清风".equals(bgcond) || "强风/劲风".equals(bgcond) || "大风".equals(bgcond) || "烈风".equals(bgcond)) {
            rlselected.setBackgroundResource(R.drawable.bg_wind);
        } else if ("阵雨".equals(bgcond) || "强阵雨".equals(bgcond) || "小雨".equals(bgcond)
                || "中雨".equals(bgcond) || "大雨".equals(bgcond) || "极端降雨".equals(bgcond) || "毛毛雨/细雨".equals(bgcond)) {
            rlselected.setBackgroundResource(R.drawable.bg_rain);
        } else if ("雷阵雨".equals(bgcond) || "强雷阵雨".equals(bgcond) || "雷阵雨伴有冰雹".equals(bgcond)
                || "暴雨".equals(bgcond) || "大暴雨".equals(bgcond) || "特大暴雨".equals(bgcond) || "冻雨".equals(bgcond)) {
            rlselected.setBackgroundResource(R.drawable.bg_thunder_storm);
        } else if ("扬沙".equals(bgcond) || "浮尘".equals(bgcond) || "火山灰".equals(bgcond)
                || "沙尘暴".equals(bgcond) || "强沙尘暴".equals(bgcond)) {
            rlselected.setBackgroundResource(R.drawable.bg_sand_storm);
        } else if ("小雪".equals(bgcond) || "中雪".equals(bgcond) || "大雪".equals(bgcond)
                || "暴雪".equals(bgcond) || "雨夹雪".equals(bgcond) || "雨雪天气".equals(bgcond) || "阵雨夹雪".equals(bgcond) || "阵雪".equals(bgcond)) {
            rlselected.setBackgroundResource(R.drawable.bg_snow);
        } else {
            rlselected.setBackgroundResource(R.drawable.bg_na);
        }
    }

}
