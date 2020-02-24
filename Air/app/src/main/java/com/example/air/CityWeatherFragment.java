package com.example.air;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.air.Bean.WeatherBean;
import com.example.air.DB.DBManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityWeatherFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = "CityWeatherFragment";
    TextView tempTv,cityTv,conditionTv,windTv,tempRangeTv,dateTv,clothIndexTv,carIndexTv,coldIndexTv,sportIndexTv,raysIndexTv;
    ImageView dayTv;
    String city;
    LinearLayout frutureLayout;
    private SharedPreferences bg_pref;
    private int bg;
    String url1 =
            "http://api.map.baidu.com/telematics/v3/weather?location=";
    String url2 = "&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";
    private List<WeatherBean.ResultsBean.IndexBean> indexlist;
    private View outLayout;

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        String info = DBManager.QueryInfoByCity(city);
        if (!TextUtils.isEmpty(info)) {
            parseShowData(info);
        }
    }

    @Override
    public void onSuccess(String result) {
        //解析并展示数据
        parseShowData(result);
        //更新数据
       int i= DBManager.UpDateInfoByCity(city,result);
        if (i<=0) {
            DBManager.AddCityInfo(city,result);
        }
    }

    private void parseShowData(String result) {
       WeatherBean weatherBean =  new Gson().fromJson(result, WeatherBean.class);
       WeatherBean.ResultsBean resultsBean = weatherBean.getResults().get(0);
        indexlist = resultsBean.getIndex();
        Log.e(TAG, indexlist.get(0).getZs());
        dateTv.setText(weatherBean.getDate());
        cityTv.setText(resultsBean.getCurrentCity());
        WeatherBean.ResultsBean.WeatherDataBean todayDateBean = resultsBean.getWeather_data().get(0);
        windTv.setText(todayDateBean.getWind());
        tempRangeTv.setText(todayDateBean.getTemperature());
        conditionTv.setText(todayDateBean.getWeather());
        String[] split = todayDateBean.getDate().split("：");
        String todayTemp = split[1].replace(")", "");
        tempTv.setText(todayTemp);
        Picasso.with(getActivity()).load(todayDateBean.getDayPictureUrl()).into(dayTv);
        List<WeatherBean.ResultsBean.WeatherDataBean> frutureList = resultsBean.getWeather_data();
        frutureList.remove(0);
        for (int i = 0; i <frutureList.size() ; i++) {
          View itemview =  LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center,null);
          itemview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
          frutureLayout.addView(itemview);
            TextView iDateTv = itemview.findViewById(R.id.iten_center_tv_date);
            TextView iconTv = itemview.findViewById(R.id.iten_center_tv_con);
            TextView itemRangeTv = itemview.findViewById(R.id.iten_center_tv_temp);
            ImageView iIv = itemview.findViewById(R.id.iten_center_iv);
            WeatherBean.ResultsBean.WeatherDataBean dataBean = frutureList.get(i);
            iDateTv.setText(dataBean.getDate());
            iconTv.setText(dataBean.getWeather());
            itemRangeTv.setText(dataBean.getTemperature());
            Picasso.with(getActivity()).load(dataBean.getDayPictureUrl()).into(iIv);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        Bundle bundle = getArguments();
        city =  bundle.getString("city");
       String url = url1+city+url2;
       LoadData(url);
        return view;
    }

    private void initView(View view) {
        tempTv = view.findViewById(R.id.frag_tv_currenttemp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        windTv = view.findViewById(R.id.frag_tv_wind);
        tempRangeTv = view.findViewById(R.id.frag_tv_temprange);
        dateTv = view.findViewById(R.id.frag_tv_date);
        clothIndexTv = view.findViewById(R.id.frag_index_dress);
        carIndexTv = view.findViewById(R.id.frag_index_washcar);
        coldIndexTv = view.findViewById(R.id.frag_index_cold);
        sportIndexTv = view.findViewById(R.id.frag_index_sport);
        raysIndexTv = view.findViewById(R.id.frag_index_rays);
        dayTv = view.findViewById(R.id.frag_tv_today);
        frutureLayout = view.findViewById(R.id.frag_center_layout);
        outLayout = view.findViewById(R.id.frag_layout);

        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        coldIndexTv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);

        bg_pref =getActivity().getSharedPreferences("bg_pref", MODE_PRIVATE);
        bg = bg_pref.getInt("bg", 2);
        switch (bg){
            case 0:
                outLayout.setBackgroundResource(R.mipmap.bg);
                break;
            case 1:
                outLayout.setBackgroundResource(R.mipmap.bg2);

                break;case 2:
                outLayout.setBackgroundResource(R.mipmap.bg3);

                break;
        }

    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (v.getId()) {
            case R.id.frag_index_cold:
                builder.setTitle("穿衣指数");
                WeatherBean.ResultsBean.IndexBean indexBean = indexlist.get(0);
                String msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_sport:
                builder.setTitle("洗车指数");
                indexBean = indexlist.get(1);
               msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_washcar:
                builder.setTitle("感冒指数");
                indexBean = indexlist.get(2);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_rays:
                builder.setTitle("运动指数");
                indexBean = indexlist.get(3);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_dress:
                builder.setTitle("紫外线指数");
                indexBean = indexlist.get(4);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
        }
        builder.create().show();
    }
}
