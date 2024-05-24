package com.octopus.android.car.apps;

import androidx.appcompat.app.AppCompatDelegate;

import com.zhuchao.android.session.MApplication;

public class MultimediaApplication extends MApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
