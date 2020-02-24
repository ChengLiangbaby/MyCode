package com.example.air;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.air.DB.DBManager;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {
TextView bgTv,cacheTv,versionTv,shareTv;
ImageView backIv;
RadioGroup radioGroup;
    private SharedPreferences bg_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        bgTv = findViewById(R.id.more_tv_exchangebg);
        cacheTv = findViewById(R.id.more_tv_cache);
        versionTv = findViewById(R.id.more_tv_version);
        shareTv = findViewById(R.id.more_tv_share);
        backIv = findViewById(R.id.more_iv_back);
        radioGroup =findViewById(R.id.more_rg);
        bgTv.setOnClickListener(this);
        cacheTv.setOnClickListener(this);
        shareTv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        String versionName = getVersionName();
        versionTv.setText("当前版本：    v"+versionName);
        bg_pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
        setRGListener();
    }

    private void setRGListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               int bg =  bg_pref.getInt("bg",0);
                SharedPreferences.Editor edit = bg_pref.edit();
                Intent intent = new Intent(MoreActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                switch (checkedId) {
                    case R.id.more_rb_green:
                        if (bg==0){
                            Toast.makeText(MoreActivity.this,"您选择的是当前背景，无需改变！",Toast.LENGTH_SHORT).show();
                        }
                        edit.putInt("bg",0);
                        edit.commit();
                        break;
                    case  R.id.more_rb_blue:
                        if (bg==1){
                            Toast.makeText(MoreActivity.this,"您选择的是当前背景，无需改变！",Toast.LENGTH_SHORT).show();
                        }
                        edit.putInt("bg",1);
                        edit.commit();
                        break;

                    case R.id.more_rb_pink:
                        if (bg==2){
                            Toast.makeText(MoreActivity.this,"您选择的是当前背景，无需改变！",Toast.LENGTH_SHORT).show();
                        }
                        edit.putInt("bg",2);
                        edit.commit();
                        break;
                }
                startActivity(intent);
            }
        });
    }

    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        String versionName = null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
return versionName;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_iv_back:
                finish();
                break;
            case R.id.more_tv_cache:
                clearCache();
                break;
            case R.id.more_tv_share:
                shareSoftwareMsg("说天气APP是一款好用的APP");
                break;
            case R.id.more_tv_exchangebg:
                if (radioGroup.getVisibility()== View.VISIBLE) {
                    radioGroup.setVisibility(View.GONE);
                }else {
                    radioGroup.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void shareSoftwareMsg(String s) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,s);
        startActivity(Intent.createChooser(intent,"说天气"));

    }

    private void clearCache() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("确定要删除所有缓存吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBManager.deleteAllInfo();
                Toast.makeText(MoreActivity.this,"已清除所有缓存",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoreActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).setNegativeButton("取消",null).create().show();
    }
}
