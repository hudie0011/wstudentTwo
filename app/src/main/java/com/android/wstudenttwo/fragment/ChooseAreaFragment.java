package com.android.wstudenttwo.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wstudenttwo.R;
import com.android.wstudenttwo.activity.WeatherActivity;
import com.android.wstudenttwo.db.City;
import com.android.wstudenttwo.db.County;
import com.android.wstudenttwo.db.Province;
import com.android.wstudenttwo.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseAreaFragment extends Fragment {
    private static final String TAG = "ChooseAreaFragment";

private Button btn_back;
private TextView txt_title;
private ListView list_view;
private ArrayAdapter adapter;
private List<String> arrlist=new ArrayList<>();

    public static final int level_province=0;
    public static final int level_city=1;
    public static final int level_county=2;
    private int current_level;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedprovince;
    private City selectedcity;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.choose_area, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        btnsetonclick();

    }


    private void initView() {
        btn_back=getActivity().findViewById(R.id.back_button);
        txt_title=getActivity().findViewById(R.id.title_text);
        list_view=getActivity().findViewById(R.id.list_view);
        adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,arrlist);
        list_view.setAdapter(adapter);
    }

    private void initData() {
        initProvince();
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(current_level==level_province){
                    selectedprovince=provinceList.get(i);
                    initCity();
                }else  if(current_level==level_city){
                    selectedcity=cityList.get(i);
                   initCounty();
                }else if(current_level==level_county){
                    Intent intent=new Intent(getActivity(),WeatherActivity.class);
                    County county=countyList.get(i);
                    String weatherid=county.getWeather_id();
                    intent.putExtra("aa",weatherid);
                    Toast.makeText(getActivity(),weatherid,Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onItemClick: "+weatherid);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            }
        });

    }



    private void initProvince() {
        txt_title.setText("中国");
        btn_back.setVisibility(View.INVISIBLE);

        //先从数据库中查询并显示
        provinceList= DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            arrlist.clear();
            for (Province province:provinceList){
                String myprovinceName=province.getProvinceName();
                arrlist.add(myprovinceName);
            }
            adapter.notifyDataSetChanged();
            list_view.setSelection(0);
            current_level=level_province;
        }else{
            //从服务器上查询，保存到数据里，然后再显示
            String url="http://guolin.tech/api/china";
            HttpUtil.sendOkHttpRequest(url, new Callback() {


                @Override
                public void onResponse(Call call, Response response) throws IOException {
                  String responsetext = response.body().string();
                    try {
                        JSONArray jsonArray=new JSONArray(responsetext);
                        for (int i =0; i <jsonArray.length() ; i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            int mycode=jsonObject.getInt("id");
                            String myname=jsonObject.getString("name");
                            Province province=new Province();
                            province.setProvinceName(myname);
                            province.setProvinceCode(mycode);
                            province.save();
                       }

                       //显示出来
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initProvince();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                @Override
                public void onFailure(Call call, IOException e) {
                    closeProgressDialog();
                    Toast.makeText(getActivity(),"网络加载失败",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    private void initCity() {
        txt_title.setText(selectedprovince.getProvinceName());
        btn_back.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("provinceid=?",String.valueOf(selectedprovince.getProvinceCode())).find(City.class);
        if(cityList.size()>0){
        arrlist.clear();
            for(City city:cityList){
            int mycode=city.getCityCode();
             String myname=city.getCityName();
             arrlist.add( mycode+"  "+ myname);
              }
              adapter.notifyDataSetChanged();
              list_view.setSelection(0);
              current_level=level_city;
            }else {
            //从服务器上查询
            String url="http://guolin.tech/api/china/"+selectedprovince.getProvinceCode();
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                   String response2= response.body().string();
                    try {
                        JSONArray jsonArray=new JSONArray(response2);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                             int mycitycode=jsonObject.getInt("id");
                             String mycityname=jsonObject.getString("name");
                             City city=new City();
                             city.setCityCode(mycitycode);
                             city.setCityName(mycityname);
                             city.setProvinceId(selectedprovince.getId());
                             city.save();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initCity();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    closeProgressDialog();
                    Toast.makeText(getActivity(),"网络加载失败",Toast.LENGTH_SHORT).show();
                }


            });
        }

    }



    private void initCounty() {
        txt_title.setText(selectedcity.getCityName());
        btn_back.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid=?",String.valueOf(selectedcity.getCityCode())).find(County.class);
        if(countyList.size()>0){
            arrlist.clear();
            for(County county:countyList){
               int mycountycode= county.getCountyCode();
               String mycountyname=county.getCountyName();
               String myweatherid=county.getWeather_id();
               arrlist.add(mycountycode+"  "+mycountyname+ "  "+myweatherid);
            }
            adapter.notifyDataSetChanged();
            list_view.setSelection(0);
            current_level=level_county;
        }else{
            String url="http://guolin.tech/api/china/"+selectedprovince.getProvinceCode()+"/"+selectedcity.getCityCode();
            showProgressDialog();
            HttpUtil.sendOkHttpRequest(url, new Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                   String responsse3= response.body().string();
                    try {
                        JSONArray jsonArray=new JSONArray(responsse3);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            int mycountycode=jsonObject.getInt("id");
                            String mycountyname=jsonObject.getString("name");
                            String myweatherid=jsonObject.getString("weather_id");

                            County county=new County();
                            county.setCountyCode(mycountycode);
                            county.setCountyName(mycountyname);
                            county.setWeather_id(myweatherid);
                            county.setCityId(selectedcity.getCityCode());
                            county.save();
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeProgressDialog();
                                initCounty();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    closeProgressDialog();
                    Toast.makeText(getActivity(),"网络加载失败",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }



    private void btnsetonclick() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current_level==level_city){
                    initProvince();
                }else if(current_level==level_county){
                    initCity();
                }
            }
        });

    }

    //访问网络时出现的进度对话框
    private void showProgressDialog() {
        if(progressDialog==null){
          progressDialog=new ProgressDialog(getActivity());
          progressDialog.setMessage("正在加载中。。。。。。");
          progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }


    //访问成功，显示之前关闭进度对话框，或者网络访问不成功也关闭进度对话框
    private void closeProgressDialog() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

}
