package com.android.wstudenttwo.util;

import com.android.wstudenttwo.bean.WeatherBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
        public static WeatherBean handleWeatherResponse(String response){
            try {
                JSONObject jsonObject=new JSONObject(response);
                String heweather=jsonObject.getString("HeWeather");
                JSONArray jsonArray=new JSONArray(heweather);
                String contentweather=jsonArray.getJSONObject(0).toString();
                Gson gson=new Gson();
                WeatherBean weatherBean=gson.fromJson(contentweather,WeatherBean.class);
                return weatherBean;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
}
