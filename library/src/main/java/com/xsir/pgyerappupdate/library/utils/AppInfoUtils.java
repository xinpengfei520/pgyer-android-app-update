package com.xsir.pgyerappupdate.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:
 */
public class AppInfoUtils {

    private static final String TAG = "AppInfoUtils";

    /**
     * 返回当前程序版本号
     */
    public static int getVersionCode(Context context) {
        String versionName = "";
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "VersionInfo" + "Exception" + e);
        }
        return versioncode;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            Log.d(TAG, "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static String getPackageName(Context context) {
        if (context == null) {
            return "";
        }
        Context appContext = context.getApplicationContext();
        return appContext.getPackageName();
    }
}
