package com.example.air;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.air.Adapter.CityManagerAdapter;
import com.example.air.DB.DBManager;
import com.example.air.DB.DataBaseBean;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener {
ImageView addIv,backIv,deleteIv;
ListView cityIv;
List<DataBaseBean> list;
    private CityManagerAdapter cityManagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        addIv = findViewById(R.id.city_iv_add);
        deleteIv = findViewById(R.id.city_iv_delete);
        backIv = findViewById(R.id.city_iv_back);
        cityIv = findViewById(R.id.city_Iv);
        list = new ArrayList<>();
        addIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        cityManagerAdapter = new CityManagerAdapter(this, list);
        cityIv.setAdapter(cityManagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<DataBaseBean> dataBaseBeans = DBManager.QueryAllInfo();
        list.clear();
        list.addAll(dataBaseBeans);
        cityManagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_iv_add:
                int cityCount = DBManager.getCityCount();
                if (cityCount<5){
                    Intent intent = new Intent(this,SearchCityActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"存储城市数量已达上限,请删除后再增加",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.city_iv_back:
                finish();
                break;
            case R.id.city_iv_delete:
                Intent intent1 = new Intent(this,DeleteCityActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
