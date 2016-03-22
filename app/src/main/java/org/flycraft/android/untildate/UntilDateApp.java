package org.flycraft.android.untildate;

import android.app.Application;
import android.util.Log;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class UntilDateApp extends Application {

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate() {
        super.onCreate();
        initAppRate();
    }

    private void initAppRate() {
        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(5) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(TAG, Integer.toString(which));
                    }
                })
                .monitor();
    }

}
