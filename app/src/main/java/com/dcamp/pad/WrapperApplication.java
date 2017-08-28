package com.dcamp.pad;

import com.easyder.wrapper.MainApplication;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Auther:  winds
 * Data:    2017/6/28
 * Desc:
 */

public class WrapperApplication extends MainApplication {

    private static final String BUGLY_APP_ID = "734c13b653";


    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), BUGLY_APP_ID, true);
    }
}
