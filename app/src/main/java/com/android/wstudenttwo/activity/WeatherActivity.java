package com.android.wstudenttwo.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wstudenttwo.R;
import com.android.wstudenttwo.bean.DailyforecastBean;
import com.android.wstudenttwo.bean.WeatherBean;
import com.android.wstudenttwo.util.HttpUtil;
import com.android.wstudenttwo.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";

private ScrollView weather_layout;
private TextView titleCity,titleUpdateTime,degree_text,weatherInfoText;
private LinearLayout forcast_layout;
private TextView aqi_text,pm25_text,comfort_text,carwash_text,sport_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initView();
        initData();
    }


    private void initView() {
weather_layout=findViewById(R.id.weather_layout);
        titleCity=findViewById(R.id.title_city11);
        titleUpdateTime=findViewById(R.id.title_update_time);
        degree_text=findViewById(R.id.degree_text);
        weatherInfoText=findViewById(R.id.weather_info_text);
        forcast_layout=findViewById(R.id.forecatst_layout);
        aqi_text=findViewById(R.id.aqi_text);
        pm25_text=findViewById(R.id.pm25_text);
        comfort_text=findViewById(R.id.comfort_text);
        carwash_text=findViewById(R.id.car_wash_text);
        sport_text=findViewById(R.id.sport_text);

    }

    private void initData() {

        String weatherid=getIntent().getStringExtra("aa");
        Log.d(TAG, "initData: "+weatherid);
        weather_layout.setVisibility(View.INVISIBLE);
        requestWeather(weatherid);
    }




    private void requestWeather(String weatherid) {
        String url="http://guolin.tech/api/weather?cityid="+weatherid+"&key=0885425e02e54661bd27eb5c6398a25d";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            final String responsetext=response.body().string();
                final WeatherBean weatherBean= Utility.handleWeatherResponse(responsetext);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weatherBean!=null&&"ok".equals(weatherBean.status)){
                            SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responsetext);
                            editor.apply();
                            Log.d(TAG, "run: "+"信息获取成功并已妥善存放");
                            showWeatherInfo(weatherBean);

                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        Toast.makeText(WeatherActivity.this,"11获取天气信息失败",Toast.LENGTH_SHORT).show();

    }
});
            }
        });
    }

    private void showWeatherInfo(WeatherBean weatherBean) {
        String cityname=weatherBean.basic.city;
        Log.d(TAG, "showWeatherInfo: 看看啥名字"+cityname);

        String updatetime=weatherBean.basic.update.loc;
        String degree=weatherBean.now.tmp+"℃";
        Log.d(TAG, "showWeatherInfo:degree: "+degree);

        String weatherInfo=weatherBean.now.cond.txt;
        titleCity.setText(cityname);
        titleUpdateTime.setText(updatetime);
        degree_text.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forcast_layout.removeAllViews();

        for(DailyforecastBean dailyforecastBean:weatherBean.daily_forecast){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forcast_layout,false);
            TextView dateText =view.findViewById(R.id.date_text11);
            TextView info_text =view.findViewById(R.id.info_text);
            TextView maxText =view.findViewById(R.id.max_text);
            TextView minText =view.findViewById(R.id.min_text);
          dateText.setText(dailyforecastBean.getDate());
            Log.d(TAG, "showWeatherInfo:dateText: "+dailyforecastBean.getDate());

            info_text.setText(dailyforecastBean.getCond().getTxt_d());
            maxText.setText(dailyforecastBean.getTmp().getMax()+"℃");
            minText.setText(dailyforecastBean.getTmp().getMin()+"℃");

            forcast_layout.addView(view);


        }

        if(aqi_text!=null){
            Log.d(TAG, "showWeatherInfo: "+(aqi_text!=null));
            aqi_text.setText(weatherBean.aqi.city.aqi+"");
            Log.d(TAG, "showWeatherInfo: "+weatherBean.aqi.city.aqi);
            pm25_text.setText(weatherBean.aqi.city.pm25+"");
            Log.d(TAG, "showWeatherInfo: "+weatherBean.aqi.city.pm25);
        }

        String comfort="舒适度："+weatherBean.suggestion.comf.txt;
        String carwash="洗车指数："+weatherBean.suggestion.cw.txt;
        String sport="运动建议："+weatherBean.suggestion.sport.txt;
        comfort_text.setText(comfort);
        carwash_text.setText(carwash);
        sport_text.setText(sport);
        Log.d(TAG, "showWeatherInfo: "+"内容显示完毕");
        Log.d(TAG, "showWeatherInfo: "+"内容显示完毕");
        weather_layout.setVisibility(View.VISIBLE);

    }
}
