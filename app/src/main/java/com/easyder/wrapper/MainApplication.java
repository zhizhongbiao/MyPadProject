package com.easyder.wrapper;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.PersistentCookieStore;


/**
 * @author 刘琛慧
 *         date 2015/10/12.
 */
public class MainApplication extends Application {

    private SharedPreferences mainPreferences;
    //主线程handler
    private static Handler mMainThreadHandler = new Handler();
    //主线程
    private static Thread mMainThread = Thread.currentThread();
    //主线程Id
    private static int mMainThreadId = android.os.Process.myTid();
    //context
    private static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        OkGo.init(this);
        try {
            OkGo.getInstance()
                    .debug("OkHttpUtils")
                    .setCookieStore(new PersistentCookieStore());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static MainApplication getInstance() {
        return instance;
    }

    /**
     * 请使用PerferenceUtils来保存数据
     *
     * @return
     */
    @Deprecated
    public static SharedPreferences getMainPreferences() {
        return getInstance().mainPreferences;
    }


}
