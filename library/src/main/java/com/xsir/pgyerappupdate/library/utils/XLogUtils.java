package com.xsir.pgyerappupdate.library.utils;

import android.util.Log;

/**
 * Created by x-sir on 2018-07-20 :)
 * Function:日志打印类工具类
 */
public class XLogUtils {

    private static final String DEFAULT_TAG = "XLogUtils";
    private static boolean IS_NEED_PRINT_LOG = true;

    public static void setIsNeedPrintLog(boolean isNeedPrintLog) {
        IS_NEED_PRINT_LOG = isNeedPrintLog;
    }

    public static void v(String msg) {
        if (IS_NEED_PRINT_LOG) {
            Log.v(DEFAULT_TAG, msg);
        }
    }

    public static void d(String msg) {
        if (IS_NEED_PRINT_LOG) {
            Log.d(DEFAULT_TAG, msg);
        }
    }

    public static void i(String msg) {
        if (IS_NEED_PRINT_LOG) {
            Log.i(DEFAULT_TAG, msg);
        }
    }

    public static void w(String msg) {
        if (IS_NEED_PRINT_LOG) {
            Log.w(DEFAULT_TAG, msg);
        }
    }

    public static void e(String msg) {
        if (IS_NEED_PRINT_LOG) {
            Log.e(DEFAULT_TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (IS_NEED_PRINT_LOG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (IS_NEED_PRINT_LOG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (IS_NEED_PRINT_LOG) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (IS_NEED_PRINT_LOG) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IS_NEED_PRINT_LOG) {
            Log.d(tag, msg);
        }
    }
}
