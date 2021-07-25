package com.example.shoppinglistapp;

import android.app.Application;

public class MyApplication extends Application {

    public static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance() {
        return MyApplication.sInstance;
    }
}
