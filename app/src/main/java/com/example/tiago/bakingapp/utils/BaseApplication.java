package com.example.tiago.bakingapp.utils;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

public class BaseApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Timber.plant(new Timber.DebugTree());
    }

    public static Context getContext(){
        return mContext;
    }
}
