package com.wanbo.werb;

import android.app.Application;
import android.content.Context;

import com.litesuits.orm.LiteOrm;

/**
 * Created by Werb on 2016/7/26.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * Application about LiteOrm
 */
public class MyApp extends Application{

    private static final String DB_NAME = "weibo.db";
    public static LiteOrm mDb;

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //init LiteOrm
        mDb = LiteOrm.newSingleInstance(this,DB_NAME);
        if (BuildConfig.DEBUG) {
            mDb.setDebugged(true);
        }

        mContext = getApplicationContext();
    }

    @Override public void onTerminate() {
        super.onTerminate();
    }
}
