package com.biner.ripplesweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.biner.ripplesweather.R;
import com.biner.ripplesweather.model.City;
import com.biner.ripplesweather.util.DBUtil;
import com.biner.ripplesweather.util.HttpUtil;
import com.biner.ripplesweather.util.Utility;

import java.util.ArrayList;
import java.util.List;


public class CityChooseActivity extends AppCompatActivity {
    private TextView tvtitle;
    private ListView lv;
    private List<String> dataList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private String selectionProvince;
    private String cityName;
    private int currentLevel;
    private List<City> provinceList;
    private List<String> arrayProvinceList = new ArrayList<>();
    private List<City> cityList;
    String cityID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citychoose);
        tvtitle = (TextView) findViewById(R.id.tvtilte);
        lv = (ListView) findViewById(R.id.lv);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, dataList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == 1) {
                    selectionProvince = arrayProvinceList.get(position);
                    queryCity();
                } else if (currentLevel == 2) {
                    cityName = dataList.get(position);
                    for (int i = 0; i < cityList.size(); i++) {
                        if (cityName.equals(cityList.get(i).getCityName())&&selectionProvince.equals(cityList.get(i).getProvinceName())) {
                            cityID = cityList.get(i).getId();
                            new writeDataWeather().execute(cityList.get(i).getId());
                        }
                    }
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        provinceList = DBUtil.loadCity();
        int size = provinceList.size();
        if (size > 0) {
            dataList.clear();
            for (int i = 0; i < size; i++) {
                if (!dataList.contains(provinceList.get(i).getProvinceName())) {
                    dataList.add(provinceList.get(i).getProvinceName());
                }
            }
            arrayProvinceList = dataList;
            adapter.notifyDataSetChanged();
            lv.setSelection(0);
            tvtitle.setText("中国");
            currentLevel = 1;
        }

    }

    private void queryCity() {
        cityList = DBUtil.loadCity();
        int size = provinceList.size();
        if (size > 0) {
            dataList.clear();
            for (int i = 0; i < size; i++) {
                if (selectionProvince.equals(cityList.get(i).getProvinceName())) {
                    dataList.add(cityList.get(i).getCityName());
                }
            }
            adapter.notifyDataSetChanged();
            lv.setSelection(0);
            tvtitle.setText(selectionProvince);
            currentLevel = 2;
        }
    }

    private class writeDataWeather extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String address = "https://api.heweather.com/x3/weather?cityid=" + params[0] + "&key=b2ad4353d8eb4e048c438de6666f1153";
            String JsonResult = HttpUtil.sendHttpRequest(address);
            return Utility.jsonWeatherResponse(JsonResult);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            closeProgressDialog();
            Intent intent = new Intent(CityChooseActivity.this, WeatherActivity.class);
            intent.putExtra("CITYID", cityID);
            startActivity(intent);
            finish();
        }

        private void showProgressDialog() {
            if (null == progressDialog) {
                progressDialog = new ProgressDialog(CityChooseActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.show();
        }

        private void closeProgressDialog() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (currentLevel == 1) {
            finish();
        } else if (currentLevel == 2) {
            queryProvinces();
        } else {
            queryCity();
        }
    }
}
