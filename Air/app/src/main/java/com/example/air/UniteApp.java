package com.example.air;

import android.app.Application;

import com.example.air.DB.DBManager;

import org.xutils.x;

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        DBManager.initDB(this);

    }
}
