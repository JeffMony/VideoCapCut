package com.jeffmony.testve;

import android.app.Application;

import com.jeffmony.media.util.DeviceUtils;

public class VeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DeviceUtils.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
