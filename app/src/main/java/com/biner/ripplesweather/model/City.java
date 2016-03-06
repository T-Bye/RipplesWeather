package com.biner.ripplesweather.model;

/**
 * Created by jb294 on 2016/2/19.
 */
public class City {
    private String id;
    private String provinceName;
    private String cityName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "id:"+getId()+"  provinceName:"+getProvinceName()+"  cityName:"+getCityName();
    }
}
