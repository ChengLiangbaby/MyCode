package com.example.air;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.air.Adapter.DeleteCityAdapter;
import com.example.air.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener {
ImageView errorIv,rightIv;
ListView listView;
List<String> mDatas;
List<String> deleteCity;
    DeleteCityAdapter deleteCityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        errorIv = findViewById(R.id.delete_iv_error);
        rightIv = findViewById(R.id.delete_iv_right);
        listView = findViewById(R.id.delete_Lv);
        mDatas = new ArrayList<>();
        deleteCity = new ArrayList<>();
        errorIv.setOnClickListener(this);
        rightIv.setOnClickListener(this);
        deleteCityAdapter  = new DeleteCityAdapter(this, mDatas, deleteCity);
        listView.setAdapter(deleteCityAdapter);


    }

    @Override
    protected void onResume() {
       super.onResume();
       List<String> cityList  = DBManager.QueryAllCityName();

         mDatas.addAll(cityList);
        Log.e( "onResume: ",mDatas.size()+"" );
        deleteCityAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_iv_error:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示信息").setMessage("您确定要舍弃更改吗？").setPositiveButton("舍弃更改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消",null);
                builder.create().show();

                break;
            case R.id.delete_iv_right:
                for (int i = 0; i < deleteCity.size(); i++) {
                    String city = deleteCity.get(i);
DBManager.deleteInfoByCity(city);
                }
                finish();
                break;
        }

    }
}
