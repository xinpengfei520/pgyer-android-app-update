package com.xsir.pgyerappupdate.library.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:
 */
public class ThreadUtils {

    public static void runOnUiThread(Runnable runnable) {
        Handler mHandler = new Handler(Looper.getMainLooper());
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
