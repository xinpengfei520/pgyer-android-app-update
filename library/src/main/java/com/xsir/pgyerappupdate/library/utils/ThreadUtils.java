package com.xsir.pgyerappupdate.library.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:线程管理工具类
 */
public class ThreadUtils {

    public static void runOnUiThread(Runnable runnable) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
