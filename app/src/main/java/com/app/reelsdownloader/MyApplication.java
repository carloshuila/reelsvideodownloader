package com.app.reelsdownloader;

import android.app.Application;

import com.hjq.permissions.XXPermissions;
import com.app.reelsdownloader.util.PermissionInterceptor;

public class MyApplication extends Application  {
    private static String TAG = MyApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        XXPermissions.setInterceptor(new PermissionInterceptor());
    }
}
