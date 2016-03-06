package com.biner.ripplesweather.util;

import com.biner.ripplesweather.model.City;
import com.biner.ripplesweather.model.FutureWeather;
import com.biner.ripplesweather.model.TodayWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Function
 * <p/>
 * Created by Biner on 2016/2/19.
 */
public class Utility {
    public synchronized static boolean jsonCityResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            City city = new City();
            JSONArray jsonArray = jsonObject.getJSONArray("city_info");
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                city.setId(object.getString("id"));
                city.setProvinceName(object.getString("prov"));
                city.setCityName(object.getString("city"));
                DBUtil.saveCity(city);
            }
            return true;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static synchronized boolean jsonWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            TodayWeather todayWeather = new TodayWeather();
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject weatherDetail = jsonArray.getJSONObject(0);

            JSONObject basic = weatherDetail.getJSONObject("basic");
            todayWeather.setCityId(basic.getString("id"));
            todayWeather.setCityName(basic.getString("city"));

            JSONObject update = basic.getJSONObject("update");
            todayWeather.setUpdate(update.getString("loc"));

            JSONObject now = weatherDetail.getJSONObject("now");
            todayWeather.setTmp(now.getString("tmp") + "°");

            JSONObject cond = now.getJSONObject("cond");
            todayWeather.setCond(cond.getString("txt"));

            JSONObject suggestion = weatherDetail.getJSONObject("suggestion");
            JSONObject comf = suggestion.getJSONObject("comf");
            todayWeather.setSuggestion_comf(comf.getString("txt"));
            JSONObject drsg = suggestion.getJSONObject("drsg");
            todayWeather.setSuggestion_drsg(drsg.getString("txt"));
            JSONObject flu = suggestion.getJSONObject("flu");
            todayWeather.setSuggestion_flu(flu.getString("txt"));
            JSONObject sport = suggestion.getJSONObject("sport");
            todayWeather.setSuggestion_sport(sport.getString("txt"));
            JSONObject trav = suggestion.getJSONObject("trav");
            todayWeather.setSuggestion_trav(trav.getString("txt"));
            JSONObject uv = suggestion.getJSONObject("uv");
            todayWeather.setSuggestion_uv(uv.getString("txt"));

            DBUtil.saveTodayWeather(todayWeather);


            JSONArray daily_forecast = weatherDetail.getJSONArray("daily_forecast");
            FutureWeather futureWeather = new FutureWeather();

            int daily_length = daily_forecast.length();
            for (int i = 0; i < daily_length; i++) {
                futureWeather.setCityId(basic.getString("id"));

                JSONObject object = daily_forecast.getJSONObject(i);

                JSONObject tmp_Max_Min = object.getJSONObject("tmp");
                futureWeather.setTmp(tmp_Max_Min.getString("min") + " ~ " + tmp_Max_Min.getString("max") + "°");

                JSONObject cond_d_n = object.getJSONObject("cond");
                futureWeather.setCond_d("cond" + cond_d_n.getString(("code_d")));
                futureWeather.setCond_n("cond" + cond_d_n.getString(("code_n")));

                JSONObject wind_dir_sc = object.getJSONObject("wind");
                futureWeather.setWind(wind_dir_sc.getString("dir") + " " + wind_dir_sc.getString("sc"));

                String data = object.getString("date");
                futureWeather.setData(data.substring(5));
                DBUtil.saveFutureWeather(futureWeather);
            }


            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
