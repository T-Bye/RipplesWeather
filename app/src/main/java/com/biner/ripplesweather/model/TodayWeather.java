package com.biner.ripplesweather.model;

import java.util.List;

/**
 * Function
 * <p/>
 * Created by Biner on 2016/2/23.
 */
public class TodayWeather {
    private String cityId;
    private String cityName;
    private String tmp;
    private String cond;
    private String update;
    private String suggestion_comf;
    private String suggestion_drsg;
    private String suggestion_flu;
    private String suggestion_sport;
    private String suggestion_trav;
    private String suggestion_uv;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getSuggestion_comf() {
        return suggestion_comf;
    }

    public void setSuggestion_comf(String suggestion_comf) {
        this.suggestion_comf = suggestion_comf;
    }

    public String getSuggestion_drsg() {
        return suggestion_drsg;
    }

    public void setSuggestion_drsg(String suggestion_drsg) {
        this.suggestion_drsg = suggestion_drsg;
    }

    public String getSuggestion_flu() {
        return suggestion_flu;
    }

    public void setSuggestion_flu(String suggestion_flu) {
        this.suggestion_flu = suggestion_flu;
    }

    public String getSuggestion_sport() {
        return suggestion_sport;
    }

    public void setSuggestion_sport(String suggestion_sport) {
        this.suggestion_sport = suggestion_sport;
    }

    public String getSuggestion_trav() {
        return suggestion_trav;
    }

    public void setSuggestion_trav(String suggestion_trav) {
        this.suggestion_trav = suggestion_trav;
    }

    public String getSuggestion_uv() {
        return suggestion_uv;
    }

    public void setSuggestion_uv(String suggestion_uv) {
        this.suggestion_uv = suggestion_uv;
    }
}
