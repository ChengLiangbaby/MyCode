package com.example.air;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.air.Adapter.CityFragmentPagerAdapter;
import com.example.air.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
ImageView addCityIv,moreIv;
LinearLayout pointLayout;
ViewPager mainVp;
List<Fragment> fragmentList;
List<String> cityList;
List<ImageView> imageViewList;
    private CityFragmentPagerAdapter adapter;
    private SharedPreferences bg_pref;
    private int bg;
    private View outLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_main);
        initView();
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);
        fragmentList = new ArrayList<>();
        cityList = DBManager.QueryAllCityName();
        imageViewList = new ArrayList<>();
        if (cityList.size()==0) {
            cityList.add("北京");
        }
        bg_pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
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
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        if (!cityList.contains(city)&&!TextUtils.isEmpty(city)) {
            cityList.add(city);
        }
        initPager();
        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mainVp.setAdapter(adapter);
        //创建小圆点
        initPoint();
        mainVp.setCurrentItem(fragmentList.size()-1);
        setPagerListener();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        List<String> list = DBManager.QueryAllCityName();
        if (list.size()==0) {
            list.add("北京");
        }
        cityList.clear();
        cityList.addAll(list);
        fragmentList.clear();
        initPager();
        adapter.notifyDataSetChanged();
        imageViewList.clear();
        pointLayout.removeAllViews();
        initPoint();

        mainVp.setCurrentItem(fragmentList.size()-1);
    }

    private void setPagerListener() {
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i <imageViewList.size() ; i++) {
                    imageViewList.get(i).setImageResource(R.mipmap.a1);
                }
                imageViewList.get(position).setImageResource(R.mipmap.a2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPoint() {
        for (int i = 0; i < fragmentList.size(); i++) {
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
           LinearLayout.LayoutParams params =  (LinearLayout.LayoutParams) pIv.getLayoutParams();
           params.setMargins(0,0,20,0);
           imageViewList.add(pIv);
           pointLayout.addView(pIv);
        }
        imageViewList.get(imageViewList.size()-1).setImageResource(R.mipmap.a2);

    }

    private void initPager() {
        for (int i = 0; i <cityList.size() ; i++) {
            CityWeatherFragment cityPager = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cityPager.setArguments(bundle);
            fragmentList.add(cityPager);

        }
    }

    private void initView() {
        addCityIv = (ImageView)findViewById(R.id.main_iv_add);
        pointLayout = (LinearLayout)findViewById(R.id.main_layout_point);
        mainVp = (ViewPager)findViewById(R.id.main_vp);
        moreIv = (ImageView)findViewById(R.id.main_iv_more);
        outLayout = findViewById(R.id.main_out_layout);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.main_iv_add:
                intent.setClass(this,CityManagerActivity.class);
                startActivity(intent);
                break;

            case R.id.main_iv_more:
intent.setClass(this,MoreActivity.class);
startActivity(intent);
                break;
                default:
        }
    }
}
