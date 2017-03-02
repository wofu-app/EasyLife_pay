package com.landicorp.android.wofupay.base;

import android.app.Application;

public class BaseApplication extends Application {

    private static BaseApplication mApplication;

    public synchronized static BaseApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

}
