package com.cx.library;

import android.app.Application;

/**
 * Created by cx on 2017/10/26.
 */

public class BaseApplication extends Application {
    BaseLifecycleCallbacks mLifecycleCallbacks=new BaseLifecycleCallbacks();
    @Override
    public void onCreate() {
        super.onCreate();
    registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }
}
