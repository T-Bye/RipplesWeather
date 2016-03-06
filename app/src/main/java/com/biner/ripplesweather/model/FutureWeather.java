package com.biner.ripplesweather.model;

/**
 * Function
 * <p/>
 * Created by Biner on 2016/2/24.
 */
public class FutureWeather {
    private String cityId;
    private String data;
    private String cond_n;
    private String cond_d;
    private String tmp;
    private String wind;

    public FutureWeather() {
    }
    public String getCond_n() {
        return cond_n;
    }

    public void setCond_n(String cond_n) {
        this.cond_n = cond_n;
    }

    public String getCond_d() {
        return cond_d;
    }

    public void setCond_d(String cond_d) {
        this.cond_d = cond_d;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
