package com.biner.ripplesweather.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Function
 *
 * Created by Biner on 2016/2/19.
 */
public class HttpUtil {
    static StringBuilder response = null;
    public static String sendHttpRequest(String address){
                HttpURLConnection connection=null;
                try {
                    URL url=new URL(address);
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    if (connection.getResponseCode()!=200){
                        return null;
                    }
                    BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    response=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                } catch (MalformedURLException e) {
                    Log.e("HttpUtil", "MalformedURLException!");
                } catch (IOException e) {
                    Log.e("HttpUtil", "IOException!");
                } finally {
                    if (connection!=null){
                        connection.disconnect();
                    }
                }
                return response.toString();
            }
}
