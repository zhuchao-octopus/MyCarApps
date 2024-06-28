package com.octopus.android.car.apps.common;

import androidx.appcompat.app.AppCompatDelegate;

import com.car.api.ApiKit;
import com.zhuchao.android.session.MApplication;

public class MultimediaApplication extends MApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        ApiKit.setContext(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
