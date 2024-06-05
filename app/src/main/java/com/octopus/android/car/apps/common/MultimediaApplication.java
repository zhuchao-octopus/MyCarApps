package com.octopus.android.car.apps.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;

import com.car.api.ApiKit;
import com.car.api.ApiMain;
import com.car.api.CarService;
import com.zhuchao.android.session.MApplication;

public class MultimediaApplication extends MApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        ApiKit.setContext(this);
        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    private class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            // 活动被创建
        }

        @Override
        public void onActivityStarted(Activity activity) {
            // 活动变为可见
        }

        @Override
        public void onActivityResumed(Activity activity) {
            // 活动启动并与用户交互
        }

        @Override
        public void onActivityPaused(Activity activity) {
            // 活动失去焦点
        }

        @Override
        public void onActivityStopped(Activity activity) {
            // 活动不再可见
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            // 活动状态要保存
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            // 活动被销毁，这可能是进程结束的标志
            int pid = android.os.Process.myPid();
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
                if (processInfo.pid == pid) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        // 应用进程仍在前台，但活动被销毁了
                    } else {
                        // 应用进程已经被终止
                        // 这里处理进程结束的逻辑
                        //关闭收音机
                        CarService.me().cmd(ApiMain.CMD_KILL_APP, "com.car.radio");
                        //切空通道
//                        ApiMain.appId(ApiMain.APP_ID_RADIO, ApiMain.APP_ID_NULL);
                    }
                }
            }

        }
    }
}
